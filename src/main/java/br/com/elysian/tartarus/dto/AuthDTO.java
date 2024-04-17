package br.com.elysian.tartarus.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;




@Getter
public class AuthDTO {

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    @Min(value = 9, message = "The password must be at least 9 characters long")
    private String password;

}
