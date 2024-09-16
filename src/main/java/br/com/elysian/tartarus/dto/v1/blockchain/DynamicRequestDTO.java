package br.com.elysian.tartarus.dto.v1.blockchain;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@Getter
public class DynamicRequestDTO extends RequestSpecDTO {

    @NonNull
    @NotBlank
    private final String contractName;

    @NonNull
    @NotBlank
    private final String version;


    public DynamicRequestDTO(@NotNull @NotBlank String contractName, @NotNull @NotBlank String version, BigInteger gasPriceWei, BigInteger maxPriorityFeePerGasGwei, BigInteger gasLimitWei, String password) {
        super(gasPriceWei, maxPriorityFeePerGasGwei, gasLimitWei, password);
        this.contractName = contractName;
        this.version = version;
    }

}
