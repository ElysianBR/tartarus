package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.dto.AuthDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AuthService {

    public AuthService login(AuthDTO auth) {

        // Lógica de validação adicional
        if (auth.getUsername().length() < 5) {
            throw new IllegalArgumentException("O nome de usuário deve ter no mínimo 5 caracteres.");
        }

        // Outras validações...
        return this;
    }

}
