package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.utils.Web3jTypeMapper;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Map;

@Getter
public class ExecuteRemote extends RequestSpecDTO {

    // TODO: Mudar parâmetros, ao invés de utilizar um array, utilizar um map com o nome do parâmetro...
    private final Map<String, ParameterDTO> parameters;
    private final String outType;

    public ExecuteRemote(Map<String, ParameterDTO> parameters, String outType, BigInteger gasPriceWei, BigInteger maxPriorityFeePerGasGwei, BigInteger gasLimitWei, String password) {
        super(gasPriceWei, maxPriorityFeePerGasGwei, gasLimitWei, password);
        this.parameters = parameters;
        this.outType = outType;
    }
}

