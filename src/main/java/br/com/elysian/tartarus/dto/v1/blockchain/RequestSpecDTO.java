package br.com.elysian.tartarus.dto.v1.blockchain;


import lombok.Getter;

import java.math.BigInteger;

@Getter
public class RequestSpecDTO {

    private final BigInteger gasPriceWei;
    private final BigInteger maxPriorityFeePerGasGwei;
    private final BigInteger gasLimitWei;
    private final String password;

    public RequestSpecDTO(BigInteger gasPriceWei, BigInteger maxPriorityFeePerGasGwei, BigInteger gasLimitWei, String password) {
        this.gasPriceWei = gasPriceWei;
        this.maxPriorityFeePerGasGwei = maxPriorityFeePerGasGwei;
        this.gasLimitWei = gasLimitWei;
        this.password = password;
    }

}
