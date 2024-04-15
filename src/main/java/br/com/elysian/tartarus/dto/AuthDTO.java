package br.com.elysian.tartarus.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class AuthDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
