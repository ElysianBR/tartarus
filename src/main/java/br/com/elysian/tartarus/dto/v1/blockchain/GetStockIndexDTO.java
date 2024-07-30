package br.com.elysian.tartarus.dto.v1.blockchain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
public class GetStockIndexDTO {

    @NotNull
    private BigInteger productId;

    @NotNull
    private  String control;

}
