package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//@JsonView(RoleViews.AccountView.class)
public class SuccessDTO {
    private boolean success;
    private String message;
}
