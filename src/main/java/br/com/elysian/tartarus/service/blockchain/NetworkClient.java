package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

public class NetworkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkClient.class);
    private @Value("#{messageService.getMessage('tartarus.blockchain.ethereum.unable.create.contract.instance')}") String instanceFail;
    private @Value("#{messageService.getMessage('tartarus.blockchain.contract.class-is-not-contract')}") String classIsNotContract;

    @Getter
    private final Web3j web3j;
    @Getter
    private final TransactionManager transactionManager;
    @Getter
    private BigInteger maxGasWei;


    public NetworkClient(Web3j web3j, TransactionManager transactionManager) {
        this.web3j = web3j;
        this.transactionManager = transactionManager;
    }

    public NetworkClient(Web3j web3j, TransactionManager transactionManager, BigInteger maxGasWei) {
        this.web3j = web3j;
        this.transactionManager = transactionManager;
        this.maxGasWei = maxGasWei;
    }


    private BigInteger getMaxGasLimit(CompanyNetworkContract companyNetworkContract) {
        if (maxGasWei != null && maxGasWei.compareTo(BigInteger.ZERO) > 0) {
            return maxGasWei;
        }

        return companyNetworkContract.getMaxGasWei();
    }


    public <T extends Contract> Class<T> getContractClass(String className) throws ContractInstantiationFailed {
        try {

            return (Class<T>) Class.forName(className);

        } catch (ClassNotFoundException e) {

            LOGGER.error(instanceFail, e);

            throw new ContractInstantiationFailed(instanceFail, e);

        } catch (ClassCastException cce) {

            throw new ContractInstantiationFailed(String.format(classIsNotContract, className));

        }
    }


    public <T extends Contract> Class<T> getContractClass(br.com.elysian.tartarus.model.Contract contractModel) throws ContractInstantiationFailed {
        return this.getContractClass(contractModel.getClassName());
    }


    private <T extends Contract> T getContractInstance(Class<T> clazz, StaticGasProvider gasProvider) throws ContractInstantiationFailed {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);
            constructor.setAccessible(true);

            String clientAddress = transactionManager.getFromAddress();
            return constructor.newInstance(clientAddress, web3j, transactionManager, gasProvider);

        } catch (Exception e) {

            LOGGER.error(instanceFail, e);

            throw new ContractInstantiationFailed(instanceFail, e);

        }
    }


    private <T extends Contract> T getContractInstance(String className, StaticGasProvider gasProvider) throws ContractInstantiationFailed {
        Class<T> clazz = this.getContractClass(className);
        return this.getContractInstance(clazz, gasProvider);
    }


    public <T extends Contract> T getContractInstance(CompanyNetworkContract companyNetworkContract) throws ContractInstantiationFailed {
        br.com.elysian.tartarus.model.Contract contractModel = companyNetworkContract.getContract();

        String className = contractModel.getClassName();
        BigInteger gasPrice = contractModel.getGasPriceWei();
        BigInteger gasLimitPrice = this.getMaxGasLimit(companyNetworkContract);
        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimitPrice);

        return this.getContractInstance(className, gasProvider);
    }


    public <T extends Contract> T getContractInstance(br.com.elysian.tartarus.model.Contract contractModel, BigInteger gasLimitPrice) throws ContractInstantiationFailed {
        String className = contractModel.getClassName();

        BigInteger gasPrice = contractModel.getGasPriceWei();
        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimitPrice);

        return this.getContractInstance(className, gasProvider);
    }


    public <T extends Contract> T deployContract(Class<T> clazz, StaticGasProvider gasProvider) throws ContractInstantiationFailed {
        try {
            T instance = this.getContractInstance(clazz, gasProvider);

            Method methodDeploy = clazz.getMethod("deploy", Web3j.class, TransactionManager.class, ContractGasProvider.class);

            return ((RemoteCall<T>) methodDeploy.invoke(instance, web3j, transactionManager, gasProvider)).send();
        } catch (Exception e) {

            LOGGER.error(instanceFail, e);

            throw new ContractInstantiationFailed(instanceFail, e);

        }
    }


    public <T extends Contract> T deployContract(String className, StaticGasProvider gasProvider) throws ContractInstantiationFailed {
        Class<T> clazz = this.getContractClass(className);
        return this.deployContract(clazz, gasProvider);
    }

}
