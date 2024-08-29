package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.model.Blockchain;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class BlockchainDTO {

    private Long id;
    private boolean active;
    private String tag;
    private String name;
    private String description;

    public BlockchainDTO(Blockchain blockchain) {
        this.id = blockchain.getId();
        this.active = blockchain.isActive();
        this.tag = blockchain.getTag();
        this.name = blockchain.getName();
        this.description = blockchain.getDescription();
    }

}
