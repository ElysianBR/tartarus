package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.ConfirmationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationPK implements Serializable {
    
    @JoinColumn(name = "account_id")
    private Account account;

    @JoinColumn(name = "type_id")
    private ConfirmationType type;
}
