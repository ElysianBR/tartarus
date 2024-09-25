package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.post.LoginDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.exception.InvalidUsernameOrPasswordException;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.AccountRole;
import br.com.elysian.tartarus.model.Role;
import br.com.elysian.tartarus.repository.AccountDAO;
import br.com.elysian.tartarus.repository.AccountRoleDAO;
import br.com.elysian.tartarus.service.jwt.JwtBuilder;
import br.com.elysian.tartarus.service.jwt.JwtService;
import br.com.elysian.tartarus.utils.PasswordUtils;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuthService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final MessageService messageService;
    private final AccountDAO accountDAO;
    private final AccountRoleDAO accountRoleDAO;
    private final JwtService jwtService;
    @Getter
    private static final String TOKEN_PREFIX = "Bearer ";
    @Getter
    private static final String HEADER_STRING = "Authorization";
    @Getter
    private static final String HEADER_EXPIRES_STRING = "Expires";

    @Autowired
    private AuthService(MessageService messageService, AccountDAO accountDAO, AccountRoleDAO accountRoleDAO, JwtService jwtUtil) {
        this.messageService = messageService;
        this.accountDAO = accountDAO;
        this.accountRoleDAO = accountRoleDAO;
        this.jwtService = jwtUtil;
    }

    public static AuthenticationAccount getCurrentAuthentication() {
        return (AuthenticationAccount) SecurityContextHolder.getContext().getAuthentication();
    }

    public static void setCurrentAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public AuthenticationAccount getAuthentication(String jwtToken) {
        try {
            DecodedJWT decodedJWT = jwtService.getDecodedJWT(jwtToken);

            Long accountId = Long.valueOf(decodedJWT.getSubject());
            Date expiresAt = decodedJWT.getExpiresAt();

            Optional<Account> accountOpt = accountDAO.findById(accountId);

            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();

                ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                for (Role role : account.getActiveRoles()) {
                    if (role.isActive()) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(role.getId()));
                    }
                }

                GrantedAuthority selectedGranted = null;
                if (getDefaultRole(account) != null) {
                    selectedGranted = new SimpleGrantedAuthority(getDefaultRole(account).getId());
                }


                CredentialAccount credential = new CredentialAccount(
                        account
                );


                Map<String, Claim> claims = decodedJWT.getClaims();
                credential.setClaims(claims);


                AuthenticationAccount authAccount = new AuthenticationAccount(
                        account.getId(),
                        credential,
                        grantedAuthorities
                );

                authAccount.setSelectedGranted(selectedGranted);
                authAccount.setExpiresAt(expiresAt);

                return authAccount;
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        throw new BadCredentialsException(messageService.getMessage("tartarus.authorization.header.invalid-bearer"));
    }


    public Role getDefaultRole(Account account) {
        List<AccountRole> roles = accountRoleDAO.findByAccountAndIsDefault(account, true);
        if (!roles.isEmpty()) {
            return roles.getFirst().getRole();
        }
        return null;
    }


    public String resolveToken(HttpServletRequest request) throws BadCredentialsException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null) {
            throw new BadCredentialsException(messageService.getMessage("tartarus.authorization.header.not-found"));
        }

        if (!header.startsWith(TOKEN_PREFIX)) {
            throw new BadCredentialsException(messageService.getMessage("tartarus.authorization.header.invalid-bearer"));
        }

        return this.resolveAuthorizationHeader(header);
    }


    public String resolveAuthorizationHeader(String authorization) {
        return authorization.replace(TOKEN_PREFIX, "");
    }

    private TokenDTO getTokenDTO(String jwtToken) {
        DecodedJWT jwt = jwtService.getDecodedJWT(jwtToken);
        return new TokenDTO(jwt.getExpiresAt(), jwtToken);
    }


    public TokenDTO getAuthorization(LoginDTO auth, boolean longLivedToken) throws InvalidUsernameOrPasswordException {
        Optional<Account> accountOpt = accountDAO.findByEmail(auth.getEmail());

        if (accountOpt.isPresent()) {

            Account account = accountOpt.get();

            if (PasswordUtils.verifyPassword(auth.getPassword(), account.getPassword())) {
                return this.getAuthorization(account, longLivedToken);
            }
        }

        throw new InvalidUsernameOrPasswordException(String.format(messageService.getMessage("tartarus.validations.email-password-not-valid"), auth.getEmail()));
    }

    public TokenDTO getAuthorization(LoginDTO auth) throws InvalidUsernameOrPasswordException {
        return this.getAuthorization(auth, false);
    }


    public TokenDTO getAuthorization(Account account, boolean longLivedToken) throws InvalidUsernameOrPasswordException {
        return this.getTokenDTO(
                jwtService.sign(
                        jwtService.getBuilder(
                                account,
                                longLivedToken
                        )
                )
        );
    }

    public TokenDTO getAuthorization(Account account) throws InvalidUsernameOrPasswordException {
        return this.getAuthorization(account, false);
    }


    public TokenDTO getAuthorization(Long accountId, boolean longLivedToken) throws InvalidUsernameOrPasswordException {
        return this.getTokenDTO(
                jwtService.sign(
                        jwtService.getBuilder(
                                accountId,
                                longLivedToken
                        )
                )
        );
    }


    public TokenDTO getAuthorization(Long accountId) throws InvalidUsernameOrPasswordException {
        return this.getAuthorization(accountId, false);
    }


    public TokenDTO getAuthorizationByCurrentAuthentication() {
        AuthenticationAccount auth = AuthService.getCurrentAuthentication();

        Long idUser = (Long) auth.getPrincipal();
        Date expiresAt = auth.getExpiresAt();

        JwtBuilder builder = jwtService.getBuilder(idUser, expiresAt);
        CredentialAccount credential = (CredentialAccount) auth.getCredentials();

        builder.addCredentialsAsClaim(credential);

        return this.getTokenDTO(
                jwtService.sign(builder)
        );
    }


    public void setHeaders(TokenDTO jwtToken, HttpServletResponse response) {
        response.setHeader("Access-Control-Expose-Headers", HEADER_STRING);
        response.addHeader(HEADER_STRING, String.format("%s %s", TOKEN_PREFIX, jwtToken.getToken()));
        response.addHeader(HEADER_EXPIRES_STRING, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(jwtToken.getExpiresIn()));
    }


}
