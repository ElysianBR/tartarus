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
import java.util.Map;
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


    private JWTCreator.Builder build(Long accountId, boolean longLivedToken) {
        System.out.println("EXPIRATION_TIME: " + EXPIRATION_TIME);
        return JWT.create()
                .withSubject(String.valueOf(accountId))
                .withExpiresAt(
                        new Date(
                                System.currentTimeMillis() +
                                        (longLivedToken ? EXPIRATION_TIME_LONG_LIVED : EXPIRATION_TIME)
                        )
                );
    }


    private JWTCreator.Builder build(Long accountId, Date expiresAt) {
        return JWT.create()
                .withSubject(String.valueOf(accountId))
                .withExpiresAt(expiresAt);
    }

    private JWTCreator.Builder build(Long accountId) {
        return this.build(accountId, false);
    }

    public JwtBuilder getBuilder(Long accountId, boolean longLivedToken) {
        return new JwtBuilder(this.build(accountId, longLivedToken));
    }

    public JwtBuilder getBuilder(Account account, boolean longLivedToken) {
        return new JwtBuilder(this.build(account.getId(), longLivedToken));
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
