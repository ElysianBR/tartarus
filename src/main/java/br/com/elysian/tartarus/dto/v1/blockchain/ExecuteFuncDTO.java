package br.com.elysian.tartarus.dto.v1.blockchain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExecuteFuncDTO {

    @NotNull
    @NotBlank
    private final String contactName;
    @NotNull
    @NotBlank
    private final String version;
    @NotNull
    @NotBlank
    private final String functionName;
    private final ParameterDTO[] parameters;

}

