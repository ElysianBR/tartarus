package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.CompanyNetwork;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkAccountPK implements Serializable {
    private CompanyNetwork companyNetwork;
    private Account account;


}
