package br.com.elysian.tartarus.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyBlockchainContractNetworkPK extends CompanyBlockchainNetworkPK implements Serializable {
    private Contract contract;
}
