package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.utils.InfuraUtils;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class NetworkClient {

    private final MessageService messageService;
    private final InfuraUtils infuraUtils;

    @Getter
    private final Credentials credentials;
    @Getter
    private final Web3j web3j;
    @Getter
    private final TransactionManager transactionManager;

    public NetworkClient(Credentials credentials, Web3j web3j, TransactionManager transactionManager, InfuraUtils infuraUtils, MessageService messageService) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.credentials = credentials;
        this.web3j = web3j;
        this.transactionManager = transactionManager;
        this.messageService = messageService;
        this.infuraUtils = infuraUtils;
    }


    public <T extends Contract> Class<T> getContractClass(String className) throws ContractInstantiationFailed {
        try {

            return (Class<T>) Class.forName(className);

        } catch (ClassNotFoundException e) {

            throw new ContractInstantiationFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        } catch (ClassCastException cce) {

            throw new ContractInstantiationFailed(String.format(messageService.getMessage("tartarus.blockchain.contract.class-is-not-contract"), className));

        }
    }


    public <T extends Contract> Class<T> getContractClass(br.com.elysian.tartarus.model.Contract contractModel) throws ContractInstantiationFailed {
        return this.getContractClass(contractModel.getClassName());
    }


    private <T extends Contract> T getContractInstance(Class<T> clazz, ContractGasProvider gasProvider) throws ContractInstantiationFailed {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);
            constructor.setAccessible(true);

            String clientAddress = transactionManager.getFromAddress();
            return constructor.newInstance(clientAddress, web3j, transactionManager, gasProvider);

        } catch (Exception e) {

            throw new ContractInstantiationFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        }
    }


    private <T extends Contract> T getContractInstance(String className, ContractGasProvider gasProvider) throws ContractInstantiationFailed {
        Class<T> clazz = this.getContractClass(className);
        return this.getContractInstance(clazz, gasProvider);
    }


    public <T extends Contract> T getContractInstance(CompanyNetworkContract companyNetworkContract) throws ContractInstantiationFailed, IOException, ExecutionException, InterruptedException {
        br.com.elysian.tartarus.model.Contract contractModel = companyNetworkContract.getContract();

        String className = contractModel.getClassName();
        ContractGasProvider gasProvider = infuraUtils.getGasProvider(BigInteger.ONE);

        return this.getContractInstance(className, gasProvider);
    }


    public <T extends Contract> T deployContract(Class<T> clazz, ContractGasProvider gasProvider) throws ContractInstantiationFailed, TransactionException {
        try {
            T instance = this.getContractInstance(clazz, gasProvider);

            Method methodDeploy = clazz.getMethod("deploy", Web3j.class, TransactionManager.class, ContractGasProvider.class);




            return ((RemoteCall<T>) methodDeploy.invoke(instance, web3j, transactionManager, gasProvider)).send();
        } catch (TransactionException te) {

            throw te;

        } catch (Exception e) {

            throw new ContractInstantiationFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        }
    }


    public <T extends Contract> T deployContract(String className, ContractGasProvider gasProvider) throws ContractInstantiationFailed, TransactionException {
        Class<T> clazz = this.getContractClass(className);
        return this.deployContract(clazz, gasProvider);
    }

}
