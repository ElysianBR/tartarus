package br.com.elysian.tartarus.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationPK implements Serializable {
    private Account account;
    private ConfirmationType type;
}
