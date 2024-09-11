package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.Contract;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkContractPK implements Serializable {
    private CompanyNetwork companyNetwork;
    private Contract contract;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyNetworkContractPK that)) return false;

        return Objects.equals(companyNetwork, that.companyNetwork) && Objects.equals(contract, that.contract);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(companyNetwork);
        result = 31 * result + Objects.hashCode(contract);
        return result;
    }
}
