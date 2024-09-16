package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
//@JsonView(RoleViews.AccountView.class)
public class TokenDTO {
    private final Date expiresIn;
    private final String token;
}
