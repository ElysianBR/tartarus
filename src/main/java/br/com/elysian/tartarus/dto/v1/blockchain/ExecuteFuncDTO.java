package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.utils.Web3jTypeMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class ExecuteFuncDTO extends RequestSpecDTO {

    private final ParameterDTO[] parameters;
    private final Web3jTypeMapper.Types outType;

    public ExecuteFuncDTO(String functionName, ParameterDTO[] parameters, Web3jTypeMapper.Types outType, BigInteger gasPriceWei, BigInteger maxPriorityFeePerGasGwei, BigInteger gasLimitWei, String password) {
        super(gasPriceWei, maxPriorityFeePerGasGwei, gasLimitWei, password);
        this.parameters = parameters;
        this.outType = outType;
    }
}

