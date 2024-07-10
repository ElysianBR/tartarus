package br.com.elysian.tartarus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "tartarus", name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "id_company", sequenceName = "company_id_seq", schema = "tartarus")
    @Column(name = "id")
    private Long id;


    @Column(name = "active")
    @ColumnDefault("false")
    private boolean active = false;


    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;


    @Column(name = "company_name", nullable = false)
    private String companyName;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "company_account",
            joinColumns = @JoinColumn(name = "id_company"),
            inverseJoinColumns = @JoinColumn(name = "id_account")
    )
    private Set<Account> accountsCompany;

}
