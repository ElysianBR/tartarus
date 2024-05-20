package br.com.elysian.tartarus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(schema = "tartarus", name = "role")
public class Role {

    @Length(max = 50)
    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "active")
    @ColumnDefault( "true" )
    private boolean active = true;

    @Column(name = "description")
    private String description;

    public Role() {
    }
}
