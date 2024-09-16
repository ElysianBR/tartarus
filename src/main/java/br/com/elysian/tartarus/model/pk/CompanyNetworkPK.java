package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.model.Network;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkPK implements Serializable {
    private Company company;
    private Network network;
}