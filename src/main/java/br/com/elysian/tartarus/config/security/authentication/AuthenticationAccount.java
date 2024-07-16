package br.com.elysian.tartarus.config.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
public class AuthenticationAccount extends UsernamePasswordAuthenticationToken {

    public AuthenticationAccount(Object principal, CredentialAccount credentials) {
        super(principal, credentials);
    }

    public AuthenticationAccount(Object principal, CredentialAccount credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
