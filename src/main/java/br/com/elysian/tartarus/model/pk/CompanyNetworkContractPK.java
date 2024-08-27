package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.Contract;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNetworkContractPK implements Serializable {
    private CompanyNetwork companyNetwork;
    private Contract contract;
}
