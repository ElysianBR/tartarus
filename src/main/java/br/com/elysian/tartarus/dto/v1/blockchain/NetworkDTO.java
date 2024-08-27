package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.Network;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class NetworkDTO {

    private Long id;
    private boolean active;
    private String tag;
    private String name;
    private String description;
    private String networkUrl;
    private NetworkTypeDTO type;
    private BlockchainDTO blockchain;

    public NetworkDTO(Network network) {
        this.id = network.getId();
        this.active = network.isActive();
        this.tag = network.getTag();
        this.name = network.getName();
        this.description = network.getDescription();
        this.networkUrl = network.getNetworkUrl();
        this.type = new NetworkTypeDTO(network.getType());
        this.blockchain = new BlockchainDTO(network.getBlockchain());
    }

}
