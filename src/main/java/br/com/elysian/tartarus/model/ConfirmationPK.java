package br.com.elysian.tartarus.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationPK implements Serializable {
    private Account account;
    private ConfirmationType type;
}
