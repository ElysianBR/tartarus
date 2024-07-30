package br.com.elysian.tartarus.service.blockchain.ethereum.contract;


import br.com.elysian.tartarus.service.blockchain.ethereum.ContractInstantiationFailed;
import br.com.elysian.tartarus.service.blockchain.ethereum.model.contract.stock.Stock;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

@Getter
public abstract class ContractImp<T extends Contract> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractImp.class);
    private @Value("#{messageService.getMessage('tartarus.blockchain.ethereum.unable.create.contract.instance')}") String messageFail;

    final private Class<T> clazz;
    final Web3j web3j;
    final String contractAddress;
    final BigInteger gasPrice;
    final BigInteger gasLimit;
    final TransactionManager transactionManager;
    final ContractGasProvider gasProvider;


    protected ContractImp(Class<T> clazz, String contractAddress, Web3j web3j, String fromAdress,
                          BigInteger gasPrice, BigInteger gasLimit) {
        this.clazz = clazz;

        this.contractAddress = contractAddress;
        this.web3j = web3j;

        // TransactionManager gerencia as transações
        this.transactionManager = new ClientTransactionManager(web3j, fromAdress);

        // Gas Provider
        this.gasProvider = new StaticGasProvider(gasPrice, gasLimit);
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }


    T load() throws ContractInstantiationFailed {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);
            constructor.setAccessible(true);
            return constructor.newInstance(contractAddress, web3j, transactionManager, gasProvider);
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            LOGGER.error(messageFail, e);
            throw new ContractInstantiationFailed(messageFail, e);
        }
    }


}
