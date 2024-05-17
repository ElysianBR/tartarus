package br.com.elysian.tartarus.utils;

import br.com.elysian.tartarus.model.Account;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.constraints.NotNull;
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
                .withClaim("firstName", account.getFirstName())
                .withClaim("lastName", account.getLastName())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public Map<String, Claim> parseJwtClaims(String token) {
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token)
                .getClaims();
    }

    public DecodedJWT getDecodedJWT(String token){
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);
    }

}
