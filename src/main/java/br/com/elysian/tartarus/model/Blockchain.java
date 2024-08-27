package br.com.elysian.tartarus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "tartarus", name = "blockchain")
public class Blockchain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "blockchain_id", sequenceName = "blockchain_id_seq", schema = "tartarus")
    @Column(name = "id", length = 10)
    private Long id;

    @Column(name = "active")
    @ColumnDefault("true")
    private boolean active = true;

    @Column(name = "tag", length = 5, nullable = false, unique = true)
    private String tag;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

}
