package br.com.elysian.tartarus.service.jwt;

import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
public class JwtBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialAccount.class);

    private final JWTCreator.Builder builder;

    @Setter
    private Long companyId;

    @Setter
    private String blochchainTag;

    public JwtBuilder(JWTCreator.Builder builder) {
        this.builder = builder;
    }

    public void addCredentialsAsClaim(CredentialAccount credential) {


        Field[] fields = credential.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            String fieldName = field.getName();

            if (!fieldName.equals("accountId")
                    && !fieldName.equals("email")
                    && !fieldName.equals("password")
            ) {
                Class<?> fieldType = field.getType();

                try {
                    if (fieldType == Boolean.class) {
                        builder.withClaim(fieldName, (Boolean) field.get(credential));
                    }

                    if (fieldType == Integer.class) {
                        builder.withClaim(fieldName, (Integer) field.get(credential));
                    }

                    if (fieldType == Long.class) {
                        builder.withClaim(fieldName, (Long) field.get(credential));
                    }

                    if (fieldType == Double.class) {
                        builder.withClaim(fieldName, (Double) field.get(credential));
                    }

                    if (fieldType == String.class) {
                        builder.withClaim(fieldName, (String) field.get(credential));
                    }

                    if (fieldType == Date.class) {
                        builder.withClaim(fieldName, (Date) field.get(credential));
                    }

                    if (fieldType == Instant.class) {
                        builder.withClaim(fieldName, (Instant) field.get(credential));
                    }

                    if (Map.class.isAssignableFrom(fieldType)) {
                        builder.withClaim(fieldName, (Map<String, ?>) field.get(credential));
                    }

                    if (List.class.isAssignableFrom(fieldType)) {
                        builder.withClaim(fieldName, (List<?>) field.get(credential));
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.warn(String.format("Field '%s', field Illegal Access Exception in this class.", fieldName));
                }

            }
        }

    }

}
