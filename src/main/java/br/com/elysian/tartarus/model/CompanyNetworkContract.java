package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.CompanyNetworkContractPK;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "tartarus", name = "company_network_contract")
//@JsonView(RoleViews.AccountView.class)
public class CompanyNetworkContract {

    @EmbeddedId
    private CompanyNetworkContractPK pk;


    @Column(name = "max_gas_wei", nullable = false)
    private BigInteger maxGasWei = BigInteger.ZERO;


    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

}
