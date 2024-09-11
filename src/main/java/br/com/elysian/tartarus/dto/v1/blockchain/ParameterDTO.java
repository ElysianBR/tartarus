package br.com.elysian.tartarus.dto.v1.blockchain;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ParameterDTO {
    private Type type;
    private Object value;

    public enum Type {
        ADDRESS,
        INT,
        STRING
    }
}