package br.com.elysian.tartarus.config.security.authentication;

import br.com.elysian.tartarus.model.Account;
import com.auth0.jwt.interfaces.Claim;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

@Getter
public class CredentialAccount {

    Logger logger = LoggerFactory.getLogger(CredentialAccount.class);

    // Account.class
    private final Long accountId;
    private final String email;
    private final String password;

    @Setter
    protected Long companyId;

    @Setter
    protected String blockchainTag;


    public CredentialAccount(Account account) {
        this.accountId = account.getId();
        this.email = account.getEmail();
        this.password = account.getPassword();
    }


    public void setClaims(Map<String, Claim> claims) {

        for (Map.Entry<String, Claim> claim : claims.entrySet()) {

            String claimName = claim.getKey();
            Claim claimValue = claim.getValue();

            if (!claimName.equals("sub")
                    && !claimName.equals("exp")
                    && !claimName.equals("accountId")
                    && !claimName.equals("email")
                    && !claimName.equals("password")
            ) {
                try {
                    Field field = this.getClass().getDeclaredField(claimName);

                    field.setAccessible(true);

                    field.set(this, claimValue.as(field.getType()));
                } catch (NoSuchFieldException e) {
                    logger.warn(String.format("Claim '%s' No Such Filed in this class.", claimName));
                } catch (IllegalAccessException e) {
                    logger.warn(String.format("Claim '%s', field Illegal Access Exception in this class.", claimName));
                }
            }

        }

    }

}
