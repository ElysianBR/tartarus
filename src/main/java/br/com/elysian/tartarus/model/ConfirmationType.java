package br.com.elysian.tartarus.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(schema = "tartarus", name = "confirmation_type")
public class ConfirmationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "confirmation_type_id", sequenceName = "confirmation_type_id_seq", schema = "tartarus")
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;
}
