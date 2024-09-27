package br.com.elysian.tartarus.dto.v1.put;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AccountDTO {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String address;
    private String city;
    private String state;
    private String country;

}
