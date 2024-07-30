package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.dto.v1.RegistrationDTO;
import br.com.elysian.tartarus.utils.PasswordUtils;
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
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "tartarus", name = "account")
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
    @Column(name = "first_name",  length = 30, nullable = false)
    private String firstName;


    @Length(max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;


    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> accountRoles;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "company_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> companies;


    @OneToMany(mappedBy = "account")
    private Set<Confirmation> confirmations;


    public boolean isEnable() {
        if (this.id == 0) {
            return true;
        }

        Optional<Confirmation> confirmation = this.getEnableConfirmation();
        return confirmation.filter(value -> value.getDateConfirmation() != null).isPresent();
    }


    public Optional<Confirmation> getEnableConfirmation() {
        for (Confirmation confirmation : confirmations) {
            if (confirmation.getType().getId() == 1) {
                return Optional.of(confirmation);
            }
        }
        return Optional.empty();
    }

    public Set<Role> getActiveRoles() {
        Set<Role> roles = new HashSet<>();
        for (Role role : this.accountRoles){
            if (role.isActive()) {
                roles.add(role);
            }
        }
        return roles;
    }


    public Account(RegistrationDTO registrationDTO) {
        this.firstName = registrationDTO.getFirstName();
        this.lastName = registrationDTO.getLastName();
        this.email = registrationDTO.getEmail();
        this.password = PasswordUtils.hashPassword(registrationDTO.getPassword());
    }
}
