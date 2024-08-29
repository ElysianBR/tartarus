package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.dto.search.RoleDTO;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class AccountDTO {

    private boolean active;
    private boolean enable;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private final Set<RoleDTO> accountRoles = new HashSet<>();

    public AccountDTO(Account account) {
        this.active = account.isActive();
        this.enable = account.isEnable();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.dateOfBirth = account.getDateOfBirth();
        this.email = account.getEmail();

        for (Role role : account.getActiveRoles()) {
            accountRoles.add(new RoleDTO(role));
        }
    }

}
