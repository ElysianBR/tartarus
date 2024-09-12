package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkContractPK implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id"),
            @JoinColumn(name = "blockchain_id", referencedColumnName = "blockchain_id")
    })
    private CompanyNetwork companyNetwork;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "name", referencedColumnName = "name"),
            @JoinColumn(name = "version", referencedColumnName = "version")
    })
    private Contract contract;
}
