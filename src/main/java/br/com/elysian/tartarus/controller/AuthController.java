package br.com.elysian.tartarus.controller;

import br.com.elysian.tartarus.annotation.SecurityXSS;
import br.com.elysian.tartarus.dto.AuthDTO;
import br.com.elysian.tartarus.model.User;
import br.com.elysian.tartarus.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @SecurityXSS
    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@Valid @RequestBody AuthDTO auth) {

        String token = authService.getAuthorization(auth);
        return ResponseEntity.ok(token);

    }

}
