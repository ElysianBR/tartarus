package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.dto.AuthDTO;
import br.com.elysian.tartarus.dto.RegistrationDTO;
import br.com.elysian.tartarus.model.User;
import br.com.elysian.tartarus.repository.UserDAO;
import br.com.elysian.tartarus.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;

    @Autowired
    private AuthService authService;

    private User user;

    @Autowired
    public UserService(UserDAO userDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.authService = authService;
    }

    public UserService saveUser(RegistrationDTO registrationDTO) {
        this.user = userDAO.save(new User(registrationDTO));
        return this;
    }

    public String getAuthorization() throws UsernameNotFoundException {
        return JwtUtil.generateToken(this.user);
    }

}
