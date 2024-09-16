package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.model.CompanyNetwork;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAccountPK implements Serializable {
    private Company company;
    private Account account;
}
