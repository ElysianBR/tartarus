package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.model.NetworkType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NetworkTypeDTO {

    private int id;
    private String typeName;

    public NetworkTypeDTO(NetworkType networkType) {
        this.id = networkType.getId();
        this.typeName = networkType.getTypeName();
    }

}
