package br.com.elysian.tartarus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "tartarus", name = "blockchain_type")
public class BlockchainType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "type_id", sequenceName = "id_type_seq", schema = "tartarus")
    @Column(name = "id")
    private int id;

    @Column(name = "type_name")
    private String typeName;

}
