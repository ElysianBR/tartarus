package br.com.elysian.tartarus.controller;

import br.com.elysian.tartarus.dto.AuthDTO;
import br.com.elysian.tartarus.service.AuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.Role;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;

@Validated
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@Valid @RequestBody AuthDTO auth) {
        String token = authService.getAuthorization(auth);

        if (token != null) {
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                .contentType(MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE))
                .body("Invalid Username/Password");

    }

}
