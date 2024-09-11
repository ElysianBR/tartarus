package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.LoginDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.exception.InvalidUsernameOrPasswordException;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Role;
import br.com.elysian.tartarus.repository.AccountDAO;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final MessageService messageService;
    private final AccountDAO accountDAO;
    private final JwtService jwtService;
    @Getter
    private static final String TOKEN_PREFIX = "Bearer ";
    @Getter
    private static final String HEADER_STRING = "Authorization";
    @Getter
    private static final String HEADER_EXPIRES_STRING = "X-Expires";

    private String jwtToken;


    @Autowired
    private AuthService(AuthenticationManager authenticationManager, MessageService messageService, AccountDAO accountDAO, JwtService jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.messageService = messageService;
        this.accountDAO = accountDAO;
        this.jwtService = jwtUtil;
    }

    public AuthenticationAccount getCurrentAuthentication() {
        return (AuthenticationAccount) SecurityContextHolder.getContext().getAuthentication();
    }

    public void setCurrentAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authenticationManager.authenticate(
                authentication
        );
    }

    public AuthenticationAccount getAuthentication(String jwtToken) {
        try {
            DecodedJWT decodedJWT = jwtService.getDecodedJWT(jwtToken);

            String email = decodedJWT.getSubject();
            String password = decodedJWT.getClaim("password").asString();

            if (email != null && password != null) {
                Optional<Account> accountOpt = accountDAO.findByEmail(email);

                if (accountOpt.isPresent()) {

                    Account account = accountOpt.get();

                    String passwordAccount = account.getPassword();

                    if (passwordAccount.equals(password)) {

                        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                        for (Role role : account.getAccountRoles()) {
                            if (role.isActive()) grantedAuthorities.add(new SimpleGrantedAuthority(role.getId()));
                        }


                        CredentialAccount credential = new CredentialAccount(
                                account
                        );


                        Map<String, Claim> claims = decodedJWT.getClaims();
                        credential.setClaims(claims);


                        return new AuthenticationAccount(
                                account.getId(),
                                credential,
                                grantedAuthorities
                        );

                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        throw new BadCredentialsException(messageService.getMessage("tartarus.authorization.header.invalid-bearer"));
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


    public TokenDTO getAuthorization(LoginDTO auth) throws InvalidUsernameOrPasswordException {
        Optional<Account> accountOpt = accountDAO.findByEmail(auth.getEmail());

        if (accountOpt.isPresent()) {

            Account account = accountOpt.get();

            if (PasswordUtils.verifyPassword(auth.getPassword(), account.getPassword())) {
                return this.getAuthorization(account);
            }
        }

        throw new InvalidUsernameOrPasswordException(String.format(messageService.getMessage("tartarus.validations.email-password-not-valid"), auth.getEmail()));
    }


    public TokenDTO getAuthorization(Account account) throws InvalidUsernameOrPasswordException {
        JwtBuilder builder = jwtService.getBuilder(account);
        this.jwtToken = jwtService.sign(builder);

        DecodedJWT jwt = jwtService.getDecodedJWT(jwtToken);
        return new TokenDTO(jwt.getExpiresAt(), jwt.getToken());
    }


    public TokenDTO getAuthorizationByCurrentAuthentication() throws InvalidUsernameOrPasswordException {
        AuthenticationAccount auth = this.getCurrentAuthentication();

        Long idUser = (Long) auth.getPrincipal();

        Optional<Account> accountOpt = accountDAO.findById(idUser);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();

            JwtBuilder builder = jwtService.getBuilder(account);
            CredentialAccount credential = (CredentialAccount) auth.getCredentials();

            builder.addCredentialsAsClaim(credential);

            this.jwtToken = jwtService.sign(builder);

            DecodedJWT jwt = jwtService.getDecodedJWT(jwtToken);
            return new TokenDTO(jwt.getExpiresAt(), jwt.getToken());
        }

        throw new AuthenticationServiceException("This authentication is not belonging to a valid account.");
    }


    public void setHeaders(TokenDTO jwtToken, HttpServletResponse response) {
        response.addHeader(HEADER_STRING, String.format("%s %s", TOKEN_PREFIX, jwtToken.getToken()));
        response.addHeader(HEADER_EXPIRES_STRING, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(jwtToken.getExpiresIn()));
    }


}
