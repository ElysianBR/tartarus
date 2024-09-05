package br.com.elysian.tartarus.service.blockchain.ethereum;


import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.InventoryManagement;
import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.ProductMovement;
import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.ProductRegistry;
import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.DeployContractDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.repository.CompanyNetworkContractDAO;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import br.com.elysian.tartarus.service.blockchain.NetworkClientOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.exceptions.TransactionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;


@Service
public class SupplyChainService {

    private final NetworkProvider networkProvider;
    private final ContractService contractService;
    private final NetworkService networkService;
    private final MessageService messageService;

    private final CompanyNetworkContractDAO companyNetworkContractDAO;


    private static final HashMap<String, Map<String, Class<? extends org.web3j.tx.Contract>>> VERSIONS = new HashMap<>();


    static {
        VERSIONS.put("v1", Map.of(
                "SupplyChainBase", SupplyChainBase.class,
                "ProductRegistry", ProductRegistry.class,
                "ProductMovement", ProductMovement.class,
                "InventoryManagement", InventoryManagement.class
        ));
    }


    @Autowired
    public SupplyChainService(NetworkProvider networkProvider, ContractService contractService, NetworkService networkService, CompanyNetworkContractDAO companyNetworkContractDAO, MessageService messageService) {
        this.networkProvider = networkProvider;
        this.contractService = contractService;
        this.networkService = networkService;
        this.companyNetworkContractDAO = companyNetworkContractDAO;
        this.messageService = messageService;
    }


    public Class<? extends org.web3j.tx.Contract> getContract(String contratName, String version) throws ContractNotFoundException {
        if (VERSIONS.containsKey(version) && VERSIONS.get(version).containsKey(contratName)) {
            return VERSIONS.get(version).get(contratName);
        }
        throw new ContractNotFoundException(messageService.getMessage("tartarus.blockchain.contract.not-found"));
    }


    public Map<ContractDeployDTO, org.web3j.tx.Contract> deployAll(String version, DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractLoadFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException, KeyStoreException, KeyManagementException {
        return Map.ofEntries(
                // TODO: Implements manual transaction DB, for if occurring errors, the beginners contracts persistent
                this.deploy("SupplyChainBase", version, deployContractDTO),
                this.deploy("ProductRegistry", version, deployContractDTO),
                this.deploy("ProductMovement", version, deployContractDTO),
                this.deploy("InventoryManagement", version, deployContractDTO)
        );
    }


    private Map.Entry<ContractDeployDTO, org.web3j.tx.Contract> deploy(String contractName, String version, DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, ContractLoadFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException, KeyStoreException, KeyManagementException, ContractNotFoundException {
        Class<? extends org.web3j.tx.Contract> clazz = this.getContract(contractName, version);

        NetworkClientOld networkClientOld = networkProvider.getNetworkClient(deployContractDTO.getPassword());
        org.web3j.tx.Contract deployOut = networkClientOld.deployContract(clazz);

        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contractName, version);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }


    private <T extends org.web3j.tx.Contract> ContractDeployDTO saveDeployInDB(T deployOut, String contractName, String version) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, ContractNotFoundException {
        Contract contract = contractService.getContract(contractName, version);
        CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();
        String contractAddress = deployOut.getContractAddress();

        CompanyNetworkContract companyNetworkContract = new CompanyNetworkContract(companyNetwork, contract, BigInteger.ZERO, contractAddress);
        return new ContractDeployDTO(
                companyNetworkContractDAO.save(companyNetworkContract)
        );
    }

}