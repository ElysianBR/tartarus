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
import br.com.elysian.tartarus.service.blockchain.ContractService;
import br.com.elysian.tartarus.service.blockchain.NetworkClient;
import br.com.elysian.tartarus.service.blockchain.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.StaticGasProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Service
public class SupplyChainService {

    private final EtherService etherService;
    private final ContractService contractService;
    private final NetworkService networkService;

    private final CompanyNetworkContractDAO companyNetworkContractDAO;


    @Autowired
    public SupplyChainService(EtherService etherService, ContractService contractService, NetworkService networkService, CompanyNetworkContractDAO companyNetworkContractDAO) {
        this.etherService = etherService;
        this.contractService = contractService;
        this.networkService = networkService;
        this.companyNetworkContractDAO = companyNetworkContractDAO;
    }


    public Map<ContractDeployDTO, org.web3j.tx.Contract> deployAll(String version, DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        return Map.ofEntries(

                // TODO: Implements manual transaction DB, for if occurring errors, the beginners contracts persistent
                this.deploySupplyChainBase(version, deployContractDTO),
                this.deployProductRegistry(version, deployContractDTO),
                this.deployProductMovement(version, deployContractDTO),
                this.deployInventoryManagement(version, deployContractDTO)
        );
    }


    private Map.Entry<ContractDeployDTO, SupplyChainBase> deploySupplyChainBase(String version, DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        System.out.println("Init SupplyChainBase");
        Contract contract = contractService.getContract("SupplyChainBase", version);
        SupplyChainBase deployOut = this.deploy(deployContractDTO.getPassword(), SupplyChainBase.class, contract, deployContractDTO.getGasPrice(), deployContractDTO.getGasLimitPerContract());
        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contract);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }


    private Map.Entry<ContractDeployDTO, ProductRegistry> deployProductRegistry(String version, DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        System.out.println("Init ProductRegistry");
        Contract contract = contractService.getContract("ProductRegistry", version);
        ProductRegistry deployOut = this.deploy(deployContractDTO.getPassword(), ProductRegistry.class, contract, deployContractDTO.getGasPrice(), deployContractDTO.getGasLimitPerContract());
        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contract);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }

    private Map.Entry<ContractDeployDTO, ProductMovement> deployProductMovement(String version, DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        System.out.println("Init ProductMovement");
        Contract contract = contractService.getContract("ProductMovement", version);
        ProductMovement deployOut = this.deploy(deployContractDTO.getPassword(), ProductMovement.class, contract, deployContractDTO.getGasPrice(), deployContractDTO.getGasLimitPerContract());
        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contract);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }

    private Map.Entry<ContractDeployDTO, InventoryManagement> deployInventoryManagement(String version, DeployContractDTO deployContractDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        System.out.println("Init InventoryManagement");
        Contract contract = contractService.getContract("InventoryManagement", version);
        InventoryManagement deployOut = this.deploy(deployContractDTO.getPassword(), InventoryManagement.class, contract, deployContractDTO.getGasPrice(), deployContractDTO.getGasLimitPerContract());
        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contract);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }


    private <T extends org.web3j.tx.Contract> ContractDeployDTO saveDeployInDB(T deployOut, Contract contract) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException {
        CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();
        String contractAddress = deployOut.getContractAddress();

        CompanyNetworkContract companyNetworkContract = new CompanyNetworkContract(companyNetwork, contract, BigInteger.ZERO, contractAddress);
        return new ContractDeployDTO(
                companyNetworkContractDAO.save(companyNetworkContract)
        );
    }


    private <T extends org.web3j.tx.Contract> T deploy(String password, Class<T> clazz, Contract contract, BigInteger gasPrice, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, ContractNotFoundException, NetworkContractNotRegistered, ContractInstantiationFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        NetworkClient networkClient = etherService.getNetworkClient(password);
        StaticGasProvider gasProvider = this.getGasProvider(contract, networkClient, gasPrice, gasLimit);
        return networkClient.deployContract(clazz, gasProvider);
    }

    private <T extends org.web3j.tx.Contract> T deploy(String password, Contract contract, BigInteger gasPrice, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, ContractNotFoundException, NetworkContractNotRegistered, ContractInstantiationFailed, TransactionException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        NetworkClient networkClient = etherService.getNetworkClient(password);
        StaticGasProvider gasProvider = this.getGasProvider(contract, networkClient, gasPrice, gasLimit);
        return networkClient.deployContract(contract.getClassName(), gasProvider);
    }


    private StaticGasProvider getGasProvider(Contract contract, NetworkClient networkClient, BigInteger gasPrice, BigInteger gasLimit) {
        BigInteger finaGasLimit = gasLimit != null ? gasLimit : networkClient.getMaxGasWei();
        return new StaticGasProvider(gasPrice, finaGasLimit);
    }

}