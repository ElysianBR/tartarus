package br.com.elysian.tartarus.dto.v1.blockchain;


import lombok.Getter;

@Getter
public class OutFuncDTO extends ExecuteFuncDTO {

    private final Object resource;

    public OutFuncDTO(String contactName, String version, String functionName, ParameterDTO[] parameters, Object resource) {
        super(contactName, version, functionName, parameters);
        this.resource = resource;
    }

}
