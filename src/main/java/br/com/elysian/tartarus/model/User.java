package br.com.elysian.tartarus.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer codUser;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String email;

    private String userName;

    private String password;

    public User() {
    }

    public User(Integer codUser, String firstName, String lastName, LocalDate dateOfBirth, String email, String userName, String password) {
        this.codUser = codUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}
