package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.ContractPK;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonView;
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
//    @JsonView({RoleViews.CompanyAdminView.class, RoleViews.AdminView.class})
    private String name;

    @Id
    @Column(name = "version")
//    @JsonView({RoleViews.CompanyAdminView.class, RoleViews.AdminView.class})
    private String version;

    @Column(name = "bin", columnDefinition = "TEXT")
//    @JsonView({RoleViews.CompanyAdminView.class, RoleViews.AdminView.class})
    private String bin;

    @Column(name = "abi", columnDefinition = "TEXT")
//    @JsonView({RoleViews.CompanyAdminView.class, RoleViews.AdminView.class})
    private String abi;

}
