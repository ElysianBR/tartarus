package br.com.elysian.tartarus.dto.v1.blockchain;


import lombok.Getter;

import java.math.BigInteger;
import java.util.Map;

@Getter
public class ExecuteRemoteDTO extends RequestSpecDTO {

    private final Map<String, ParameterDTO> parameters;
    private final String outType;

    public ExecuteRemoteDTO(Map<String, ParameterDTO> parameters, String outType, BigInteger gasPriceWei, BigInteger maxPriorityFeePerGasGwei, BigInteger gasLimitWei, String password) {
        super(gasPriceWei, maxPriorityFeePerGasGwei, gasLimitWei, password);
        this.parameters = parameters;
        this.outType = outType;
    }
}

