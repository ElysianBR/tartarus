package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.CompanyNetworkAccount;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

@Getter
public class NetworkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkClient.class);
    private @Value("#{messageService.getMessage('tartarus.blockchain.ethereum.unable.create.contract.instance')}") String instanceFail;
    private @Value("#{messageService.getMessage('tartarus.blockchain.contract.class-is-not-contract')}") String classIsNotContract;


    private final Web3j web3j;
    private final TransactionManager transactionManager;
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


    public <T extends Contract> T getContract(CompanyNetworkContract companyNetworkContract) throws ContractInstantiationFailed {
        br.com.elysian.tartarus.model.Contract contractModel = companyNetworkContract.getContract();
        String className = contractModel.getClassName();

        try {

            Class<T> clazz = (Class<T>) Class.forName(className);


            Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);
            constructor.setAccessible(true);


            BigInteger gasPrice = contractModel.getGasPriceWei();
            BigInteger gasLimitContract = this.getMaxGasLimit(companyNetworkContract);


            StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimitContract);


            String clientAddress = transactionManager.getFromAddress();
            return constructor.newInstance(clientAddress, web3j, transactionManager, gasProvider);


        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException |
                 ClassNotFoundException e) {
            ;

            LOGGER.error(instanceFail, e);

            throw new ContractInstantiationFailed(instanceFail, e);

        } catch (ClassCastException cce) {

            throw new ContractInstantiationFailed(String.format(classIsNotContract, className));

        }
    }


}
