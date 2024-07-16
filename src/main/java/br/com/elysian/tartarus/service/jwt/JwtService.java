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

@Service
@ConfigurationProperties(prefix = "tartatus.jwt")
public class JwtService {

    @Value("${tartatus.jwt.secret}")
    private String secret;

    @Getter
    @Value("${tartatus.jwt.expiration-time}")
    private long EXPIRATION_TIME;


    private JWTCreator.Builder build(Account account) {
        return JWT.create()
                .withSubject(account.getEmail())
                .withClaim("password", account.getPassword())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
    }

    public JwtBuilder getBuilder(Account account) {
        return new JwtBuilder(this.build(account));
    }

    public String sign(JwtBuilder builder){
        return builder.getBuilder().sign(Algorithm.HMAC512(secret.getBytes()));
    }


    public DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);
    }

}
