package br.com.elysian.tartarus.dto.search;

import br.com.elysian.tartarus.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleDTO {

    private String role;
    private String description;

    public RoleDTO(Role role) {
        this.role = role.getId();
        this.description = role.getDescription();
    }
}
