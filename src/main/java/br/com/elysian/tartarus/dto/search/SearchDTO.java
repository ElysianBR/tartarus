package br.com.elysian.tartarus.dto.search;

import br.com.elysian.tartarus.dto.search.criteria.FieldCriteriaDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SearchDTO<T> {

    @NotNull
    private FieldCriteriaDTO<T>[] criterias;

    private String[] expectedsFields;

}
