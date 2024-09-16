package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.CompanyNetworkPK;
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
@IdClass(CompanyNetworkPK.class)
@Table(schema = "tartarus", name = "company_network")
public class CompanyNetwork {

    @Id
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Id
    @OneToOne
    @JoinColumn(name = "blockchain_id")
    private Network network;


    @Column(name = "max_gas_wei", nullable = false)
    private BigInteger maxGasWei = BigInteger.ZERO;


}