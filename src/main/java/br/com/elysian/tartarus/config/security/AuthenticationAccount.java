package br.com.elysian.tartarus.config.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class AuthenticationAccount extends UsernamePasswordAuthenticationToken {

     private Long idCompany;

    public AuthenticationAccount(Object principal, Object credentials, Long idCompany) {
        super(principal, credentials);
        this.idCompany = idCompany;
    }

    public AuthenticationAccount(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long idCompany) {
        super(principal, credentials, authorities);
        this.idCompany = idCompany;
    }

    public AuthenticationAccount(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AuthenticationAccount(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
