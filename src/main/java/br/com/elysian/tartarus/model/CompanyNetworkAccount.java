package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.CompanyNetworkAccountPK;
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
@IdClass(CompanyNetworkAccountPK.class)
@Table(schema = "tartarus", name = "company_network_account")
public class CompanyNetworkAccount {

    @Id
    @OneToOne
    private CompanyNetwork companyNetwork;

    @Id
    @OneToOne
    private Account account;


    @Column(name = "address")
    private String address;


    @Column(name = "private")
    private String privateKey;


    @Column(name = "max_gas_wei", nullable = false)
    private BigInteger maxGasWei = BigInteger.ZERO;

}