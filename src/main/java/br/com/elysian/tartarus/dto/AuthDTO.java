package br.com.elysian.tartarus.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class AuthDTO {

    @NotNull
    @NotBlank
    private String identity;

    @NotNull
    @NotBlank
    private String password;

}
