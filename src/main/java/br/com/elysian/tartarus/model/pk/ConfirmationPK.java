package br.com.elysian.tartarus.model.pk;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.ConfirmationType;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationPK implements Serializable {
    private Account account;
    private ConfirmationType type;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfirmationPK that)) return false;

        return Objects.equals(account, that.account) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(account);
        result = 31 * result + Objects.hashCode(type);
        return result;
    }
}
