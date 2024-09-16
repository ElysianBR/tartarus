package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.CompanyNetwork;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkAccountPK implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id"),
            @JoinColumn(name = "blockchain_id", referencedColumnName = "blockchain_id")
    })
    private CompanyNetwork companyNetwork;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
