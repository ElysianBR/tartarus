package br.com.elysian.tartarus.model;

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
@IdClass(CompanyBlockchainContractNetworkPK.class)
@Table(schema = "tartarus", name = "company_blockchain_contracts")
public class CompanyBlockchainContract {

    @Id
    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Id
    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Id
    @OneToOne
    @JoinColumn(name = "blockchain_network_id")
    private BlockchainNetwork blockchainNetwork;


    @Column(name = "max_gas_wei", nullable = false)
    private BigInteger maxGasWei = BigInteger.ZERO;


    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

}
