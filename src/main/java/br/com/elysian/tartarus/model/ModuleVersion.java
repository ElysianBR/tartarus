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
    @SequenceGenerator(name = "contract_id", sequenceName = "contract_id_seq", schema = "tartarus")
    @Column(name = "module_id")
    private Long id;

    @Id
    @ManyToOne
    @JoinColumn(name = "blockchain_id")
    private Blockchain blockchain;

    @Column(name = "module_tag", length = 30, unique = true)
    private String moduleTag;

    @Id
    @Column(name = "name")
    private String name;


    @Column(name = "package")
    private String packagezz;

}
