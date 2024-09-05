package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.exception.ContractLoadFailed;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.utils.BlockchainUtils;
import br.com.elysian.tartarus.utils.InfuraUtils;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class NetworkClient {

    private final MessageService messageService;

    @Getter
    private final Credentials credentials;
    @Getter
    private final Web3j web3j;
    @Getter
    private final TransactionManager transactionManager;

    public NetworkClient(Credentials credentials, Web3j web3j, TransactionManager transactionManager, MessageService messageService) {
        this.credentials = credentials;
        this.web3j = web3j;
        this.transactionManager = transactionManager;
        this.messageService = messageService;
    }


    public AbstractContract loadContract(br.com.elysian.tartarus.model.Contract contract, ContractGasProvider gasProvider) throws ContractLoadFailed {
        try {
            String clientAddress = transactionManager.getFromAddress();
            String binary = contract.getBin();

            if (credentials != null) {

                return new AbstractContract(binary, clientAddress, web3j, credentials, gasProvider);

            } else {

                return new AbstractContract(binary, clientAddress, web3j, transactionManager, gasProvider);

            }

        } catch (Exception e) {

            throw new ContractLoadFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        }
    }


    public <T extends Contract> T deployContract(AbstractContract contract, Class<T> clazz) throws ContractLoadFailed, TransactionException {
        try {

            return contract.deploy(clazz).send();

        } catch (TransactionException te) {

            throw te;

        } catch (Exception e) {

            throw new ContractLoadFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        }
    }

}
