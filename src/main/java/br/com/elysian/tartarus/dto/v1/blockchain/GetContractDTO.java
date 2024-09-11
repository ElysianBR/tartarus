package br.com.elysian.tartarus.dto.v1.blockchain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class GetContractDTO {

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    private final String version;

}
