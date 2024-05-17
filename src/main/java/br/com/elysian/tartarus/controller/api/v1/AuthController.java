package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.annotation.SecurityXSS;
import br.com.elysian.tartarus.dto.LoginDTO;
import br.com.elysian.tartarus.dto.TokenDTO;
import br.com.elysian.tartarus.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@Transactional
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @SecurityXSS
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO auth, HttpServletResponse response) {
        TokenDTO token = authService.getAuthorization(auth);
        authService.setHeaders(token, response);
        return ResponseEntity.ok(token);
    }

}
