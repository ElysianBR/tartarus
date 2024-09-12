package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountRolePK implements Serializable {

    @JoinColumn(name = "account_id")
    private Account account;

    @JoinColumn(name = "role_id")
    private Role role;

}