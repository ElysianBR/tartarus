package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.model.Network;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkPK implements Serializable {
    private Company company;
    private Network network;
}