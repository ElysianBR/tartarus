package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.dto.RegistrationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Entity
@Table(schema = "tartarus", name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_user")
    private Integer codUser;

    @Column(name = "ativo", columnDefinition = "default false")
    private boolean ativo;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    private String role;

    public User() {
    }

    public User(RegistrationDTO registrationDTO) {
        this.firstName = registrationDTO.getFirstName();
        this.lastName = registrationDTO.getLastName();
        this.email = registrationDTO.getEmail();
        this.password = registrationDTO.getPassword();
    }
}
