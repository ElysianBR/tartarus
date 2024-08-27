package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.CompanyNetworkContractPK;
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
@IdClass(CompanyNetworkContractPK.class)
@Table(schema = "tartarus", name = "company_network_contract")
public class CompanyNetworkContract {

    @Id
    @OneToOne
    private CompanyNetwork companyNetwork;

    @Id
    @OneToOne
    private Contract contract;


    @Column(name = "max_gas_wei", nullable = false)
    private BigInteger maxGasWei = BigInteger.ZERO;


    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

}
