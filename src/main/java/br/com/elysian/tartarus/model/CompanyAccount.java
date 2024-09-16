package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.CompanyAccountPK;
import br.com.elysian.tartarus.model.pk.CompanyNetworkPK;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@IdClass(CompanyAccountPK.class)
@Table(schema = "tartarus", name = "company_account")
public class CompanyAccount {

    @Id
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Id
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


}