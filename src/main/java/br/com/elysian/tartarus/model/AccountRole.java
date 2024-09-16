package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.constraint.UniqueDefaultRole;
import br.com.elysian.tartarus.model.pk.AccountRolePK;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.ConstraintComposition;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(AccountRolePK.class)
@Table(schema = "tartarus", name = "account_role")
@UniqueDefaultRole
public class AccountRole {

    @Id
    @ManyToOne
    private Account account;

    @Id
    @ManyToOne
    private Role role;

    @Column(name = "isDefault")
    private Boolean isDefault;

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault != null ? isDefault : false;
    }
}
