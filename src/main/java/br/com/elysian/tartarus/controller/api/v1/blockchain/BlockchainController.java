package br.com.elysian.tartarus.controller.api.v1.blockchain;


import br.com.elysian.tartarus.dto.v1.blockchain.ContractDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.RequestSpecDTO;
import br.com.elysian.tartarus.model.Contract;
import br.com.elysian.tartarus.service.blockchain.ContractClient;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@Controller
@RequestMapping("/api/v1/blockchain")
public class BlockchainController {


    private final ContractService contractService;


    @Autowired
    public BlockchainController(ContractService contractService) {
        this.contractService = contractService;
    }


    @PreAuthorize("hasAnyAuthority('BUSINESS_ADMIN')")
    @GetMapping("/{module}/{version}/{contractName}")
    public ResponseEntity<ContractDTO> getContract(@PathVariable String module, @PathVariable String version, @PathVariable String contractName) throws Exception {
        return ResponseEntity.ok(
                new ContractDTO(
                        contractService.getNetworkContract(contractName, version).getPk().getContract()
                )
        );
    }


    @PreAuthorize("hasAnyAuthority('BUSINESS_ADMIN')")
    @GetMapping("/{module}/{version}/{contractName}/metadata")
    public ResponseEntity<ContractDTO> getMetadata(@PathVariable String module, @PathVariable String version, @PathVariable String contractName) throws Exception {
        return ResponseEntity.ok(
                new ContractDTO(
                        contractService.getContract(contractName, version)
                )
        );
    }


    @Async
    @Transactional
    @PreAuthorize("hasAnyAuthority('BUSINESS_ADMIN')")
    @PostMapping("/{module}/{version}/{contractName}/deploy")
    public CompletableFuture<ResponseEntity<ContractDeployDTO>> deploy(@PathVariable String module, @PathVariable String version, @PathVariable String contractName, @RequestBody RequestSpecDTO requestSpecDTO) throws Exception {
        ContractClient client = contractService.getContractClient(requestSpecDTO.getPassword());
        Contract contract = contractService.getContract(contractName, version);

        return CompletableFuture.completedFuture(
                ResponseEntity.ok(
                        new ContractDeployDTO(
                                contractService.saveDeployInDB(
                                        client.deploy(contract, requestSpecDTO)
                                                .sendAsync()
                                                .get(),
                                        contractName, version
                                )
                        )
                )
        );
    }

}
