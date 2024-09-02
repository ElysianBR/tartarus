package br.com.elysian.tartarus.dto.v1.blockchain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class DeployContractDTO {

    @NotNull
    @NotBlank
    private final BigInteger gasPrice;

    private final BigInteger gasLimitPerContract;

    @NotNull
    @NotBlank
    private final String password;

}
