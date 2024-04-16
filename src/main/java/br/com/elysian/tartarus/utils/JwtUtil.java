package br.com.elysian.tartarus.utils;

import br.com.elysian.tartarus.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET = "SECRET_KEY_HERE";
    private static final long EXPIRATION_TIME = 4 * 60 * 60 * 1000; // 4 hours


    public static String generateToken(User user) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("firstName", user.getFirstName())
                .withClaim("lastName", user.getLastName())
                .withClaim("role", user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public Map<String, Claim> parseJwtClaims(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getClaims();
    }


    public static String extractUsername(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

}
