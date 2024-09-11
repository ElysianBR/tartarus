package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.model.Contract;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContractDTO {

    private final String name;
    private final String version;
    private final String bin;
    private final String aba;

    public ContractDTO(Contract contract) {
        this.name = contract.getName();
        this.version = contract.getVersion();
        this.bin = contract.getBin();
        this.aba = contract.getAba();
    }

}
