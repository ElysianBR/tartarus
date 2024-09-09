package br.com.elysian.tartarus.controller.api.v1.blockchain;


import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.DeployContractDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.model.Contract;
import br.com.elysian.tartarus.service.blockchain.AbstractContract;
import br.com.elysian.tartarus.service.blockchain.ContractClient;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import br.com.elysian.tartarus.service.blockchain.NetworkService;
import br.com.elysian.tartarus.service.blockchain.ethereum.SupplyChainService;
import br.com.elysian.tartarus.utils.BlockchainUtils;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.web3j.protocol.core.methods.response.TransactionReceipt;


import java.io.IOException;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/blockchain/supply-chain")
public class SupplyChainController {


    private final NetworkService networkService;
    private final ContractService contractService;
    private final SupplyChainService supplyChainService;


    public SupplyChainController(NetworkService networkService, ContractService contractService, SupplyChainService supplyChainService) {
        this.networkService = networkService;
        this.contractService = contractService;
        this.supplyChainService = supplyChainService;
    }

    @Deprecated
    @PostMapping("/deploySupplyChainBase")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public CompletableFuture<ResponseEntity<ContractDeployDTO>> deploySupplyChainBase(@RequestBody DeployContractDTO deployContractDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ContractClient client = contractService.getContractClient(deployContractDTO.getPassword());
        Contract contract = contractService.getContract("SupplyChainBase", "v1");

        CompletableFuture<AbstractContract> abstractContract = client.deploy(contract);

        return CompletableFuture.completedFuture(
                ResponseEntity.ok(
                        supplyChainService.saveDeployInDB(
                                abstractContract.get(), "SupplyChainBase", "v1"
                        )
                )
        );
    }

    @PostMapping("/testSession")
    public void testSession(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Simula um longo processamento
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000); // Espera 1 segundo
                try {
                    if (response.isCommitted()) {
                        // O cliente se desconectou
                        System.out.println("Cliente desconectou antes do processamento ser concluído.");
                        break;
                    }
                } catch (IllegalStateException e) {
                    System.out.println("IllegalStateException - Cliente desconectou antes do processamento ser concluído.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping("/register-consumers-access")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TransactionReceipt> registerConsumersAccess(@RequestParam("consumerAddress") String consumerAddress, @RequestParam("access") String access) throws Exception {
        //TODO: Refactor @RequestParam to @RequestBody.

        ContractClient client = contractService.getContractClient();
        CompanyNetworkContract contract = contractService.getNetworkContract("SupplyChainBase", "v1");
        AbstractContract abstractContract = client.loadContract(contract);

        SupplyChainBase supplyChainBase = (SupplyChainBase) new Object();

        TransactionReceipt response = supplyChainBase.registerConsumersAccess(consumerAddress, access).send();

        // TODO: Not response complete response from blockchain, is necessary treatment
        return ResponseEntity.ok(response);
    }

}
