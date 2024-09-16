package br.com.elysian.tartarus.dto.v1.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class LoginDTO {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

}
