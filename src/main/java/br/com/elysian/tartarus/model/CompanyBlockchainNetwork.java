package br.com.elysian.tartarus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CompanyBlockchainPK.class)
@Table(schema = "tartarus", name = "company_blockchain")
public class CompanyBlockchain {

    @Id
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Id
    @OneToOne
    @JoinColumn(name = "blockchain_id")
    private Blockchain blockchain;


    @Column(name = "max_gas_wei", nullable = false)
    private BigInteger maxGasWei = BigInteger.ZERO;

}
