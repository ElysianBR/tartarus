package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.model.Account;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@NonNull
@NotBlank
@AllArgsConstructor
public class AccountDTO {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;

}
