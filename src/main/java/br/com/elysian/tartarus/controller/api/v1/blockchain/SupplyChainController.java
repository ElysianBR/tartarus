package br.com.elysian.tartarus.controller.api.v1.blockchain;


import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.RequestSpecDTO;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.model.Contract;
import br.com.elysian.tartarus.service.blockchain.AbstractContract;
import br.com.elysian.tartarus.service.blockchain.ContractClient;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import br.com.elysian.tartarus.service.blockchain.SupplyChainService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticEIP1559GasProvider;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/blockchain/supply-chain")
public class SupplyChainController {

    private final ContractService contractService;
    private final SupplyChainService supplyChainService;


    public SupplyChainController(ContractService contractService, SupplyChainService supplyChainService) {
        this.contractService = contractService;
        this.supplyChainService = supplyChainService;
    }


    @Async
    @Deprecated
    @Transactional
    @PostMapping("/deploy/supply-chain-base/v1")
    @PreAuthorize("hasAnyAuthority('BUSINESS_ADMIN')")
    public CompletableFuture<ResponseEntity<ContractDeployDTO>> deploySupplyChainBase(@RequestBody RequestSpecDTO requestSpecDTO) throws Exception {
        ContractClient client = contractService.getContractClient(requestSpecDTO.getPassword());

        return CompletableFuture.completedFuture(ResponseEntity.ok(
                supplyChainService.saveDeployInDB(
                        SupplyChainBase.deploy(
                                client.getWeb3j(),
                                client.getTransactionManager(),
                                new DefaultGasProvider()
                        ).sendAsync().get(),
                        "SupplyChainBase", "v1"
                )
        ));
    }


    @PostMapping("/register-consumers-access")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TransactionReceipt> registerConsumersAccess(@RequestParam("consumerAddress") String consumerAddress, @RequestParam("access") String access) throws Exception {
        //TODO: Refactor @RequestParam to @RequestBody.

        ContractClient client = contractService.getContractClient("User@12345");
        CompanyNetworkContract contract = contractService.getNetworkContract("SupplyChainBase", "v1");

        TransactionReceipt response = SupplyChainBase.load(contract.getContractAddress(), client.getWeb3j(), client.getTransactionManager(), new DefaultGasProvider())
                .registerConsumersAccess(consumerAddress, access).send();

        // TODO: Not response complete response from blockchain, is necessary treatment
        return ResponseEntity.ok(response);
    }

}
