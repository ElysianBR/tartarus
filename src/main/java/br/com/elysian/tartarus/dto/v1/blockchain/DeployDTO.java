package br.com.elysian.tartarus.dto.v1.blockchain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class DeployDTO {

    private BigInteger gasLimit;

    @NonNull
    private String contractName;

    @NonNull
    private String version;

}
