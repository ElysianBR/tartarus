package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.dto.v1.post.RegisterDTO;
import br.com.elysian.tartarus.dto.v1.put.AccountDTO;
import br.com.elysian.tartarus.utils.PasswordUtils;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "tartarus", name = "account")
//@JsonView(RoleViews.AccountView.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "account_id", sequenceName = "account_id_seq", schema = "tartarus")
    @Column(name = "id")
    private Long id;


    @Column(name = "active")
    @ColumnDefault("false")
    private boolean active = false;


    @Length(max = 30)
    @Column(name = "first_name", length = 30, nullable = false)
    private String firstName;


    @Length(max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;


    @Column(name = "profile_pic", columnDefinition = "TEXT")
    private String profilePic;


    @Column(name = "address")
    private String address;


    @Column(name = "city")
    private String city;


    @Column(name = "state")
    private String state;


    @Column(name = "country")
    private String country;


    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;


    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<AccountRole> accountRoles;


    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Confirmation> confirmations;


    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<CompanyAccount> companies;


    public boolean isEnable() {
        if (this.id == 0) {
            return true;
        }

        Optional<Confirmation> confirmation = this.getEnableConfirmation();
        return confirmation.filter(value -> value.getDateConfirmation() != null).isPresent();
    }


    @JsonIgnore
    public Optional<Confirmation> getEnableConfirmation() {
        for (Confirmation confirmation : confirmations) {
            if (confirmation.getType().getId() == 1) {
                return Optional.of(confirmation);
            }
        }
        return Optional.empty();
    }


    @JsonIgnore
    public Set<Role> getActiveRoles() {
        Set<Role> roles = new HashSet<>();
        for (AccountRole role : this.accountRoles) {
            if (role.getRole().isActive()) {
                roles.add(role.getRole());
            }
        }
        return roles;
    }


    @JsonIgnore
    public Role getDefaultRole() {
        Iterator<AccountRole> iterator = this.accountRoles.iterator();
        if (iterator.hasNext()) {
            for (AccountRole role : this.accountRoles) {
                if (role.getIsDefault()) {
                    return role.getRole();
                }
            }
            return this.accountRoles.iterator().next().getRole();
        }
        return null;
    }


    public Account(RegisterDTO registerDTO) {
        this.firstName = registerDTO.getFirstName();
        this.lastName = registerDTO.getLastName();
        this.email = registerDTO.getEmail();
        this.password = PasswordUtils.hashPassword(registerDTO.getPassword());
    }

    @JsonIgnore
    public void setAccountFromDTO(AccountDTO accountDTO) {
        // TODO: Implements set Model from dynamic DTO.
        this.firstName = accountDTO.getFirstName();
        this.lastName = accountDTO.getLastName();
        this.email = accountDTO.getEmail();
        this.dateOfBirth = accountDTO.getDateOfBirth();
    }

}
