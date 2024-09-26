package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.utils.Web3jTypeMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ParameterDTO {
    private String type;
    private Object value;

    public ParameterDTO(Web3jTypeMapper.Types types, Object value) {
        this.type = types.name();
        this.value = types.name();
    }
}