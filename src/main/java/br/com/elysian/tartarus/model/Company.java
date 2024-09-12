package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.dto.v1.PutCompanyDTO;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "tartarus", name = "company")
public class Company {

    @Id
    @JsonView(RoleViews.AdminView.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "company_id", sequenceName = "company_id_seq", schema = "tartarus")
    @Column(name = "id")
    private Long id;

    @ColumnDefault("true")
    @Column(name = "active")
    @JsonView(RoleViews.CompanyAdminView.class)
    private boolean active = true;

    @JsonView(RoleViews.AccountView.class)
    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @JsonView(RoleViews.AccountView.class)
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @JsonIgnore
    public void setCompanyFromdDTO(PutCompanyDTO companyDTO) {
        this.cnpj = companyDTO.getCnpj();
        this.companyName = companyDTO.getCompanyName();
    }

}
