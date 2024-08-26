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
@Table(schema = "tartarus", name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "contract_id", sequenceName = "contract_id_seq", schema = "tartarus")
    @Column(name = "id")
    private Long id;

    @Id
    @ManyToOne
    @JoinColumn(name = "blockchain_id")
    private Blockchain blockchain;

    @Id
    @Column(name = "version")
    private String version;

    @Id
    @Column(name = "name")
    private String name;


    @Column(name = "class")
    private String clazz;


    @Column(name = "gas_price_wei", nullable = false)
    private BigInteger gasPriceWei = BigInteger.ZERO;

}
