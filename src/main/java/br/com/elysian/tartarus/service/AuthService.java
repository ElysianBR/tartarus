package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.AuthenticationAccount;
import br.com.elysian.tartarus.dto.v1.LoginDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Role;
import br.com.elysian.tartarus.repository.UserDAO;
import br.com.elysian.tartarus.service.jwt.JwtUtil;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Service
public class AuthService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final MessageService messageService;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    @Getter
    private static final String TOKEN_PREFIX = "Bearer ";
    @Getter
    private static final String HEADER_STRING = "Authorization";
    @Getter
    private static final String HEADER_EXPIRES_STRING = "X-Expires";

    private String token;


    @Autowired
    private AuthService(MessageService messageService, UserDAO userDAO, JwtUtil jwtUtil) {
        this.messageService = messageService;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
    }


    public AuthenticationAccount getAuthentication(String token) {
        try {
            DecodedJWT decodedJWT = jwtUtil.getDecodedJWT(token);

            String email = decodedJWT.getSubject();
            String password = decodedJWT.getClaim("password").asString();

            if (email != null && password != null) {
                boolean existsAccount = userDAO.existsByEmailAndPassword(email, password);

                if (existsAccount) {
                    Account account = userDAO.findByEmailAndPassword(email, password);

                    ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                    for (Role role : account.getAccountRoles()) {
                        if (role.isActive()) grantedAuthorities.add(new SimpleGrantedAuthority(role.getId()));
                    }


                    Claim idCompanyClaim = decodedJWT.getClaim("idCompanie");
                    Long idCompanie = null;
                    if (!idCompanyClaim.isNull()) idCompanie = decodedJWT.getClaim("idCompanie").asLong();


                    return new AuthenticationAccount(
                            account.getId(),
                            account.getPassword(),
                            grantedAuthorities,
                            idCompanie
                    );
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


    public TokenDTO getAuthorization(LoginDTO auth) throws UsernameNotFoundException {


        if (userDAO.existsByEmailAndPassword(auth.getEmail(), auth.getPassword())) {

            Account account = userDAO.findByEmailAndPassword(auth.getEmail(), auth.getPassword());
            return this.getAuthorization(account);

        }

        throw new UsernameNotFoundException(String.format(messageService.getMessage("tartarus.validations.email.password.not-valid"), auth.getEmail()));
    }


    public TokenDTO getAuthorization(Account account) throws UsernameNotFoundException {
        this.token = jwtUtil.generateToken(account);
        DecodedJWT jwt = jwtUtil.getDecodedJWT(token);
        return new TokenDTO(jwt.getExpiresAt(), jwt.getToken());
    }


    public void setHeaders(TokenDTO token, HttpServletResponse response) {
        response.addHeader(HEADER_STRING, String.format("%s %s", TOKEN_PREFIX, token.getToken()));
        response.addHeader(HEADER_EXPIRES_STRING, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(token.getExpiresIn()));
    }


}
