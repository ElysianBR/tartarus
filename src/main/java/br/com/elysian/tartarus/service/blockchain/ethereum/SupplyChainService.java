package br.com.elysian.tartarus.service.blockchain.ethereum;


import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.InventoryManagement;
import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.ProductMovement;
import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.ProductRegistry;
import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.repository.CompanyNetworkContractDAO;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import br.com.elysian.tartarus.service.blockchain.NetworkClient;
import br.com.elysian.tartarus.service.blockchain.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Map;


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


    public Map<ContractDeployDTO, org.web3j.tx.Contract> deployAll(String version, BigInteger gasLimitPerContract) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed {
        return Map.ofEntries(

                this.deploySupplyChainBase(version, gasLimitPerContract),
                this.deployProductRegistry(version, gasLimitPerContract),
                this.deployProductMovement(version, gasLimitPerContract),
                this.deployInventoryManagement(version, gasLimitPerContract)

        );
    }


    public Map.Entry<ContractDeployDTO, SupplyChainBase> deploySupplyChainBase(String version, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed {
        Contract contract = this.getContract("SupplyChainBase", version);
        SupplyChainBase deployOut = this.deploy(SupplyChainBase.class, contract, gasLimit);
        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contract);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }


    public Map.Entry<ContractDeployDTO, ProductRegistry> deployProductRegistry(String version, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed {
        Contract contract = this.getContract("ProductRegistry", version);
        ProductRegistry deployOut = this.deploy(ProductRegistry.class, contract, gasLimit);
        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contract);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }

    public Map.Entry<ContractDeployDTO, ProductMovement> deployProductMovement(String version, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed {
        Contract contract = this.getContract("ProductMovement", version);
        ProductMovement deployOut = this.deploy(ProductMovement.class, contract, gasLimit);
        ContractDeployDTO deployDTO = this.saveDeployInDB(deployOut, contract);
        return new AbstractMap.SimpleEntry<>(deployDTO, deployOut);
    }

    public Map.Entry<ContractDeployDTO, InventoryManagement> deployInventoryManagement(String version, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered, AccountNotRegisteredInCompanyNetworkException, ContractInstantiationFailed {
        Contract contract = this.getContract("InventoryManagement", version);
        InventoryManagement deployOut = this.deploy(InventoryManagement.class, contract, gasLimit);
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


    private <T extends org.web3j.tx.Contract> T deploy(Class<T> clazz, Contract contract, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, ContractNotFoundException, NetworkContractNotRegistered, ContractInstantiationFailed {
        NetworkClient networkClient = etherService.getNetworkClient();
        StaticGasProvider gasProvider = this.getGasProvider(contract, networkClient, gasLimit);
        return networkClient.deployContract(clazz, gasProvider);
    }

    private <T extends org.web3j.tx.Contract> T deploy(Contract contract, BigInteger gasLimit) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, ContractNotFoundException, NetworkContractNotRegistered, ContractInstantiationFailed {
        NetworkClient networkClient = etherService.getNetworkClient();
        StaticGasProvider gasProvider = this.getGasProvider(contract, networkClient, gasLimit);
        return networkClient.deployContract(contract.getClassName(), gasProvider);
    }


    private Contract getContract(String name, String version) throws NetworkNotLoggedInException, CompanyNotLoggedInException, ContractNotFoundException, NetworkNotFoundException, NetworkContractNotRegistered {
        CompanyNetworkContract companyNetworkContract = contractService.getNetworkContract(name, version);
        return companyNetworkContract.getContract();
    }


    private StaticGasProvider getGasProvider(Contract contract, NetworkClient networkClient, BigInteger gasLimit) {
        BigInteger finaGasLimit = gasLimit != null ? gasLimit : networkClient.getMaxGasWei();
        return new StaticGasProvider(contract.getGasPriceWei(), finaGasLimit);
    }

}