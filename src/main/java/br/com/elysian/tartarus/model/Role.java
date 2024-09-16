package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
@Table(schema = "tartarus", name = "role")
//@JsonView(RoleViews.AccountView.class)
public class Role {

    @Length(max = 50)
    @Id
    @Column(name = "id", length = 50)
    private String id;

    @JsonIgnore
    @Column(name = "active")
    @ColumnDefault( "true" )
    private boolean active = true;

    @Column(name = "description")
    private String description;

}
