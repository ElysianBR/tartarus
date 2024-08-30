package br.com.elysian.tartarus.controller.api.v1.blockchain;


import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import br.com.elysian.tartarus.service.blockchain.ethereum.EtherService;
import br.com.elysian.tartarus.service.blockchain.ethereum.SupplyChainService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.TransactionReceipt;


import java.math.BigInteger;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/blockchain/supply-chain")
public class SupplyChainController {


    private final EtherService etherService;
    private final ContractService contractService;
    private final SupplyChainService supplyChainService;


    public SupplyChainController(EtherService etherService, ContractService contractService, SupplyChainService supplyChainService) {
        this.etherService = etherService;
        this.contractService = contractService;
        this.supplyChainService = supplyChainService;
    }


    @PostMapping("/deployAll")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<ArrayList<ContractDeployDTO>> deployAll(@RequestParam(value = "gasLimitPerContract", required = false) BigInteger gasLimitPerContract) throws Exception {
        return ResponseEntity.ok(
                new ArrayList<>(
                        supplyChainService.deployAll("v1", gasLimitPerContract).keySet()
                )
        );
    }


    @PostMapping("/register-consumers-access")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TransactionReceipt> registerConsumersAccess(@RequestParam("consumerAddress") String consumerAddress, @RequestParam("access") String access) throws Exception {
        CompanyNetworkContract companyNetworkContract = contractService.getNetworkContract("SupplyChainBase", "v1");
        SupplyChainBase supplyChainBase = etherService.getNetworkClient().getContractInstance(companyNetworkContract);

        TransactionReceipt response = supplyChainBase.registerConsumersAccess(consumerAddress, access).send();

        // TODO: Not response complete response from blockchain, is necessary treatment
        return ResponseEntity.ok(response);
    }

}
