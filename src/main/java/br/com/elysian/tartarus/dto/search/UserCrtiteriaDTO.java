package br.com.elysian.tartarus.dto.search;

import br.com.elysian.tartarus.dto.search.criteria.FieldCriteriaDTO;
import lombok.Getter;

@Getter
public class UserCrtiteriaDTO {

    private FieldCriteriaDTO<String> id;
    private FieldCriteriaDTO<String> email;
    private FieldCriteriaDTO<String> firstName;
    private FieldCriteriaDTO<String> lastName;
    private FieldCriteriaDTO<Boolean> active;


}
