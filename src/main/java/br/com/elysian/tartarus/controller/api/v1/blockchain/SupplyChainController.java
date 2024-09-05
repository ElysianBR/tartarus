package br.com.elysian.tartarus.controller.api.v1.blockchain;


import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.DeployContractDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.model.Contract;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import br.com.elysian.tartarus.service.blockchain.NetworkService;
import br.com.elysian.tartarus.service.blockchain.ethereum.SupplyChainService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.DefaultGasProvider;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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


    @Async
    @Deprecated
    @PostMapping("/deployAll")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public CompletableFuture<ResponseEntity<ArrayList<ContractDeployDTO>>> deployAll(@RequestBody DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, InvalidUsernameOrPasswordException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NetworkContractNotRegistered, ContractLoadFailed, TransactionException, ContractNotFoundException, NetworkNotFoundException, PrivateKeyNotSet, IOException, KeyStoreException, ExecutionException, InterruptedException, KeyManagementException {
        // TODO: Implements security validation for user persist de private key encrypted with password and send only password for validation in DB.

        return CompletableFuture.completedFuture(ResponseEntity.ok(
                new ArrayList<>(
                        supplyChainService.deployAll("v1", deployContractDTO).keySet()
                )));
    }


    @Deprecated
    @PostMapping("/register-consumers-access")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TransactionReceipt> registerConsumersAccess(@RequestParam("consumerAddress") String consumerAddress, @RequestParam("access") String access) throws Exception {
        //TODO: Refactor @RequestParam to @RequestBody.

        CompanyNetworkContract companyNetworkContract = contractService.getNetworkContract("SupplyChainBase", "v1");
        Contract contract = companyNetworkContract.getContract();
//        SupplyChainBase supplyChainBase = networkService.getNetworkClient().loadContract(contract, new DefaultGasProvider());
        SupplyChainBase supplyChainBase = (SupplyChainBase) new Object();
        TransactionReceipt response = supplyChainBase.registerConsumersAccess(consumerAddress, access).send();

        // TODO: Not response complete response from blockchain, is necessary treatment
        return ResponseEntity.ok(response);
    }

}
