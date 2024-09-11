package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.model.Network;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkPK implements Serializable {
    private Company company;
    private Network network;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyNetworkPK that)) return false;

        return Objects.equals(company, that.company) && Objects.equals(network, that.network);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(company);
        result = 31 * result + Objects.hashCode(network);
        return result;
    }
}