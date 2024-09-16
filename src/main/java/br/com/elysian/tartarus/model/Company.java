package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.dto.v1.put.CompanyDTO;
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
//@JsonView(RoleViews.AccountView.class)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "company_id", sequenceName = "company_id_seq", schema = "tartarus")
    @Column(name = "id")
    private Long id;

    @ColumnDefault("true")
    @Column(name = "active")
    private boolean active = true;

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @JsonIgnore
    public void setCompanyFromdDTO(CompanyDTO companyDTO) {
        this.cnpj = companyDTO.getCnpj();
        this.companyName = companyDTO.getCompanyName();
    }

}
