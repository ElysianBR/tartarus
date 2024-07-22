package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.service.blockchain.ContractInteract;
import br.com.elysian.tartarus.service.blockchain.ethereum.sepolia.OwnerContract;
import br.com.elysian.tartarus.service.blockchain.ethereum.sepolia.SepoliaClient;
import br.com.elysian.tartarus.service.blockchain.model.owner.Owner;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/v1/blochain")
public class BlockchainController {

    private final SepoliaClient sepoliaClient;

    @Autowired
    public BlockchainController(SepoliaClient sepoliaClient) {
        this.sepoliaClient = sepoliaClient;
    }


    @GetMapping("/getOwner")
    public ResponseEntity<String> getOwner() {
        OwnerContract ownerContract = new OwnerContract(this.sepoliaClient.getWeb3j());

        ContractInteract<Owner> owner = new ContractInteract<>(ownerContract.getOwner());

        AtomicReference<String> ownerString = new AtomicReference<>();

        owner.executeTxt((o) -> {
            try {
                ownerString.set(o.getOwner().send());
            } catch (Exception e) {
                LoggerFactory.getLogger(BlockchainController.class).error("Erro durante a transação", e);
                throw new RuntimeException(e);
            }
        });


        return ResponseEntity.ok("Owner: " + ownerString.get());
    }


}
