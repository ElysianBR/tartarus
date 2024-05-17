package br.com.elysian.tartarus.dto;

import br.com.elysian.tartarus.service.MessageService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class RegistrationDTO {

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String firstName;

    @NotNull
    @NotBlank
    @Length(max = 50)
    private String lastName;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Length(min = 9)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{9,}$", message="{tartarus.validations.invalid-password}")
    private String password;

}
