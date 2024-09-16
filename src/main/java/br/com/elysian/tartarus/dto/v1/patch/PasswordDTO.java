package br.com.elysian.tartarus.dto.v1.patch;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class PasswordDTO {

    @NotNull
    private String oldPassword;

    @NotNull
    @Length(min = 9)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{9,}$", message="{tartarus.validations.invalid-password}")
    private String newPassword;

}
