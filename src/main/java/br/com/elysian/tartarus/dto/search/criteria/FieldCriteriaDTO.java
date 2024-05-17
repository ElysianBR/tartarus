package br.com.elysian.tartarus.dto.search.criteria;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter

@AllArgsConstructor
public class FieldCriteriaDTO<T> {

    @NotNull
    private final String fieldName;

    @NotNull
    private final T value;


}
