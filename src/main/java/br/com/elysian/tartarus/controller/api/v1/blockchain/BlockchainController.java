package br.com.elysian.tartarus.controller.api.v1.blockchain;


import br.com.elysian.tartarus.dto.v1.blockchain.ExecuteRemoteDTO;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.model.Contract;
import br.com.elysian.tartarus.service.blockchain.AbstractContract;
import br.com.elysian.tartarus.service.blockchain.ContractClient;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;


@Controller
@RequestMapping("/api/v1/blockchain")
@Tag(name = "Blockchain", description = "Create and manage contracts on the blockchain by executing functions dynamically")
public class BlockchainController {


    private final ContractService contractService;


    @Autowired
    public BlockchainController(ContractService contractService) {
        this.contractService = contractService;
    }


    @Operation
    @JsonIgnoreProperties( value = {"bin", "abi"})
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMIN')")
    @GetMapping("/{module}/{version}/{contractName}")
    public ResponseEntity<CompanyNetworkContract> getContract(@PathVariable String module, @PathVariable String version, @PathVariable String contractName) throws Exception {
        return ResponseEntity.ok(
                contractService.getNetworkContract(contractName, version)

        );
    }


    @Operation
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMIN')")
    @GetMapping("/{module}/{version}/{contractName}/metadata")
    public ResponseEntity<Contract> getMetadata(@PathVariable String module, @PathVariable String version, @PathVariable String contractName) throws Exception {
        return ResponseEntity.ok(
                contractService.getContract(contractName, version)
        );
    }


    @Operation
    @Async
    @Transactional
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMIN')")
    @PostMapping("/{module}/{version}/{contractName}/deploy")
    public CompletableFuture<ResponseEntity<CompanyNetworkContract>> deploy(@PathVariable String module, @PathVariable String version, @PathVariable String contractName, @RequestBody ExecuteRemoteDTO executeRemoteDTO) throws Exception {
        ContractClient client = contractService.getContractClient(executeRemoteDTO.getPassword());
        Contract contract = contractService.getContract(contractName, version);

        return CompletableFuture.completedFuture(
                ResponseEntity.ok(
                        contractService.saveDeployInDB(
                                client.deploy(contract, executeRemoteDTO)
                                        .sendAsync()
                                        .get(),
                                contractName, version
                        )
                )
        );
    }


    @Operation
    @Async
    @Transactional
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMIN')")
    @PostMapping("/{module}/{version}/{contractName}/{functionName}")
    public CompletableFuture<ResponseEntity<TransactionReceipt>> transact(@PathVariable String module, @PathVariable String version, @PathVariable String contractName, @PathVariable String functionName, @RequestBody ExecuteRemoteDTO executeRemoteDTO) throws Exception {
        ContractClient client = contractService.getContractClient(executeRemoteDTO.getPassword());
        CompanyNetworkContract contract = contractService.getNetworkContract(contractName, version);
        AbstractContract abstractContract = client.loadContract(contract);
        Function fun = abstractContract.buildFunction(functionName, executeRemoteDTO.getParameters());

        return CompletableFuture.completedFuture(
                ResponseEntity.ok(
                        abstractContract
                                .executeFunction(
                                        fun,
                                        abstractContract.estimateFunctionGas(fun, executeRemoteDTO)
                                ).sendAsync()
                                .get()
                )
        );
    }

}
