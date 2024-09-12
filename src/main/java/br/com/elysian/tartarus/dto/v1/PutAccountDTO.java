package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.model.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PutAccountDTO {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;

    public PutAccountDTO(Account account) {
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.dateOfBirth = account.getDateOfBirth();
        this.email = account.getEmail();
    }

}
