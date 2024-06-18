package br.com.elysian.tartarus.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class TokenDTO {
    private final Date expiresIn;
    private final String token;
}
