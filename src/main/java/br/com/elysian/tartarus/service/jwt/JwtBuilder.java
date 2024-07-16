package br.com.elysian.tartarus.service.jwt;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class JwtBuilder {

    private final JWTCreator.Builder builder;

    @Getter
    @Setter
    private Long companyId;

    public JwtBuilder(JWTCreator.Builder builder) {
        this.builder = builder;
    }

    public JWTCreator.Builder getBuilder() {
        if (this.companyId != null){
            this.builder.withClaim("companyId", companyId);
        }

        return this.builder;
    }

}
