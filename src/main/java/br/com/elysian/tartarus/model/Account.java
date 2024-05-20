package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.dto.RegistrationDTO;
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
    @SequenceGenerator(name = "id_account", sequenceName = "account_id_seq", schema = "tartarus")
    @Column(name = "id")
    @JsonIgnore
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
            joinColumns = @JoinColumn(name = "id_account"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<Role> accountRoles;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "business_account",
            joinColumns = @JoinColumn(name = "id_account"),
            inverseJoinColumns = @JoinColumn(name = "id_business")
    )
    private Set<Business> business;


    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Confirmation> confirmations;


    @JsonProperty
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


    public Account(RegistrationDTO registrationDTO) {
        this.firstName = registrationDTO.getFirstName();
        this.lastName = registrationDTO.getLastName();
        this.email = registrationDTO.getEmail();
        this.password = registrationDTO.getPassword();
    }
}
