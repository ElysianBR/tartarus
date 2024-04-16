package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.dto.AuthDTO;
import br.com.elysian.tartarus.model.User;
import br.com.elysian.tartarus.repository.UserDAO;
import br.com.elysian.tartarus.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

@Service
@Validated
public class AuthService {
    private final UserDAO userDAO;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";


    @Autowired
    private AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = AuthService.resolveToken(request);
        String user = JwtUtil.extractUsername(token);

        if (user != null) {
            // Verificar se além do usuário estar no JWT token, validar se o mesmo é válido e entre outras infromações.
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }

        throw new BadCredentialsException("Invalid Bearer Token");

    }

    private static String resolveToken(HttpServletRequest request) throws BadCredentialsException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null) {
            throw new BadCredentialsException("Authorization Header not Found");
        }

        if (!header.startsWith(TOKEN_PREFIX)) {
            throw new BadCredentialsException("Bearer Token not Found");
        }

        return header.replace(TOKEN_PREFIX, "");
    }


    public String getAuthorization(AuthDTO auth) {
        User user = userDAO.findByUserNameAndPassword(auth.getUsername(), auth.getPassword());

        if (user != null) {
            UserDetails userDetails =
                    org.springframework.security.core.userdetails.User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .roles(user.getRole())
                            .build();

            return JwtUtil.generateToken(user);
        }


        return null;
    }

}
