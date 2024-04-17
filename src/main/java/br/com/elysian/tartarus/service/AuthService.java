package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.dto.AuthDTO;
import br.com.elysian.tartarus.model.User;
import br.com.elysian.tartarus.repository.UserDAO;
import br.com.elysian.tartarus.utils.JwtUtil;
import br.com.elysian.tartarus.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

@Service
public class AuthService {

    private final MessageService messageService;
    private final UserDAO userDAO;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";


    @Autowired
    private AuthService(MessageService messageService, UserDAO userDAO) {
        this.messageService = messageService;
        this.userDAO = userDAO;
    }


    public Authentication getAuthentication(HttpServletRequest request) {
        String token = this.resolveToken(request);
        String user = JwtUtil.extractUsername(token);

        if (user != null) {
            // Verificar se além do usuário estar no JWT token, validar se o mesmo é válido e entre outras infromações.
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }

        throw new BadCredentialsException(messageService.getMessage("tartarus.invalid.bearer.token"));

    }

    private String resolveToken(HttpServletRequest request) throws BadCredentialsException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null) {
            throw new BadCredentialsException(messageService.getMessage("tartarus.authorization.header.not.found"));
        }

        if (!header.startsWith(TOKEN_PREFIX)) {
            throw new BadCredentialsException(messageService.getMessage("tartarus.invalid.bearer.token"));
        }

        return header.replace(TOKEN_PREFIX, "");
    }


    public String getAuthorization(AuthDTO auth) throws UsernameNotFoundException {
        User user = userDAO.findByUsernameAndPassword(auth.getUsername(), auth.getPassword());

        if (user != null) {
            return JwtUtil.generateToken(user);
        }


        throw new UsernameNotFoundException(String.format(messageService.getMessage("tartarus.user.not.found"), auth.getUsername()));
    }

}
