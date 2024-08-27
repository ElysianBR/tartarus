package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.ContractPK;
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
@IdClass(ContractPK.class)
@Table(schema = "tartarus", name = "contract")
public class Contract {

    @Id
    @Column(name = "name")
    private String name;

    @Id
    @Column(name = "version")
    private String version;

    @Column(name = "className", unique = true)
    private String className;

    @Column(name = "gas_price_wei", nullable = false)
    private BigInteger gasPriceWei = BigInteger.ZERO;

}
