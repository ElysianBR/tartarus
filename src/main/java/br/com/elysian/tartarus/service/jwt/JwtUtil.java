package br.com.elysian.tartarus.service.jwt;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "tartatus.jwt")
public class JwtUtil {

    @Value("${tartatus.jwt.secret}")
    private String secret;
    @Getter
    private static final long EXPIRATION_TIME = 4 * 60 * 60 * 1000; // 4 hours

    public String generateToken(@NotNull Account account) {
        return JWT.create()
                .withSubject(account.getEmail())
                .withClaim("password", account.getPassword())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }


    public String generateToken(@NotNull Account account, @Null Company company) {
        return JWT.create()
                .withSubject(account.getEmail())
                .withClaim("password", account.getPassword())
                .withClaim("company", company != null ? company.getId() : null)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }


    public Map<String, Claim> parseJwtClaims(String token) {
        return this.getDecodedJWT(token).getClaims();
    }

    public DecodedJWT getDecodedJWT(String token){
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);
    }

}
