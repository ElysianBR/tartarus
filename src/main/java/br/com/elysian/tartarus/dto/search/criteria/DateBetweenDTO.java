package br.com.elysian.tartarus.dto.search.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class DateBetweenDTO {

    private final Date initialDate;
    private final Date finalDate;
    private final boolean trunc;

}
