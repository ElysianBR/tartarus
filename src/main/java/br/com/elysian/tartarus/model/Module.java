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
@Table(schema = "tartarus", name = "module_contract")
public class ModuleContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "module_contract_id", sequenceName = "module_contract_id_seq", schema = "tartarus")
    @Column(name = "id", length = 10)
    private Long id;

    @Id
    @OneToOne
    @JoinColumn(name = "blockchain_id")
    private Blockchain blockchain;

    @Column(name = "tag", length = 30, unique = true)
    private String tag;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
