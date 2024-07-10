package br.com.elysian.tartarus.service.jwt;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@ConfigurationProperties(prefix = "tartatus.jwt")
public class JWTService {

    @Value("${tartatus.jwt.secret}")
    private String SECRETE;

    @Getter
    @Value("${tartatus.jwt.expiration-time}")
    private long EXPIRATION_TIME;

    public String generateToken(@NotNull Account account) {
        return JWT.create()
                .withSubject(account.getEmail())
                .withClaim("password", account.getPassword())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRETE.getBytes()));
    }


    public String generateToken(@NotNull Account account, @Null Company company) {
        return JWT.create()
                .withSubject(account.getEmail())
                .withClaim("password", account.getPassword())
                .withClaim("company", company != null ? company.getId() : null)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRETE.getBytes()));
    }
}
