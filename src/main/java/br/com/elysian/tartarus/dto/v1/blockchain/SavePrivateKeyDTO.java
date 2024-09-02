package br.com.elysian.tartarus.dto.v1.blockchain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SavePrivateKeyDTO {

    @NotNull
    @NotBlank
    private final String privateKey;

    @NotNull
    @NotBlank
    private final String password;

}
