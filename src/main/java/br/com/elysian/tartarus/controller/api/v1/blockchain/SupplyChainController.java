package br.com.elysian.tartarus.controller.api.v1.blockchain;


import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.service.blockchain.ContractClient;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/blockchain/supply-chain")
public class SupplyChainController {

    private final ContractService contractService;

    public SupplyChainController(ContractService contractService) {
        this.contractService = contractService;
    }


    @Async
    @PostMapping("/register-consumers-access")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public CompletableFuture<ResponseEntity<TransactionReceipt>> registerConsumersAccess(@RequestParam("consumerAddress") String consumerAddress, @RequestParam("access") String access) throws Exception {
        //TODO: Refactor @RequestParam to @RequestBody.

        ContractClient client = contractService.getContractClient("User@12345");
        CompanyNetworkContract contract = contractService.getNetworkContract("SupplyChainBase", "v1");

        TransactionReceipt response = SupplyChainBase.load(contract.getContractAddress(), client.getWeb3j(), client.getTransactionManager(), new DefaultGasProvider())
                .registerConsumersAccess(consumerAddress, access).sendAsync().get();


        return CompletableFuture.completedFuture(ResponseEntity.ok(response));
    }

}
