package br.com.elysian.tartarus.service.jwt;

import br.com.elysian.tartarus.model.Account;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
@ConfigurationProperties(prefix = "tartatus.jwt")
public class JwtService {

    @Value("${tartatus.jwt.secret}")
    private String secret;

    @Getter
    @Value("${tartatus.jwt.expiration-time}")
    private long EXPIRATION_TIME;

    @Getter
    @Value("${tartatus.jwt.long-lived-expiration-time}")
    private long EXPIRATION_TIME_LONG_LIVED;

    private final HashMap<ExpirationType, Long> expirations = new HashMap<>();

    @Getter
    public enum ExpirationType {
        SIMPLE_EXPIRATION,
        LONG_EXPIRATION
    }


    private JwtService() {
        expirations.put(ExpirationType.SIMPLE_EXPIRATION, EXPIRATION_TIME);
        expirations.put(ExpirationType.LONG_EXPIRATION, EXPIRATION_TIME_LONG_LIVED);
    }


    private JWTCreator.Builder build(Long accountId, ExpirationType expirationType) {
        return JWT.create()
                .withSubject(String.valueOf(accountId))
                .withExpiresAt(
                        new Date(
                                System.currentTimeMillis() + expirations.get(expirationType)
                        )
                );
    }


    private JWTCreator.Builder build(Long accountId, Date expiresAt) {
        return JWT.create()
                .withSubject(String.valueOf(accountId))
                .withExpiresAt(expiresAt);
    }

    private JWTCreator.Builder build(Long accountId) {
        return this.build(accountId, ExpirationType.SIMPLE_EXPIRATION);
    }

    public JwtBuilder getBuilder(Long accountId, ExpirationType expirationType) {
        return new JwtBuilder(this.build(accountId, expirationType));
    }

    public JwtBuilder getBuilder(Account account, ExpirationType expirationType) {
        return new JwtBuilder(this.build(account.getId(), expirationType));
    }

    public JwtBuilder getBuilder(Long accountId) {
        return new JwtBuilder(this.build(accountId));
    }

    public JwtBuilder getBuilder(Account account) {
        return new JwtBuilder(this.build(account.getId()));
    }

    public JwtBuilder getBuilder(Long accountId, Date expiresAt) {
        return new JwtBuilder(this.build(accountId, expiresAt));
    }

    public JwtBuilder getBuilder(Account account, Date expiresAt) {
        return new JwtBuilder(this.build(account.getId(), expiresAt));
    }

    public String sign(JwtBuilder builder) {
        return builder.getBuilder().sign(Algorithm.HMAC512(secret.getBytes()));
    }


    public DecodedJWT getDecodedJWT(String token) {
        return JWT
                .require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);
    }

}
