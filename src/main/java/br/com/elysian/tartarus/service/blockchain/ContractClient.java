package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.exception.ContractLoadFailed;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.utils.BlockchainUtils;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;

public class ContractClient {

    private final MessageService messageService;

    @Getter
    private final Credentials credentials;
    @Getter
    private final Web3j web3j;
    @Getter
    private final TransactionManager transactionManager;

    public ContractClient(Credentials credentials, Web3j web3j, TransactionManager transactionManager, MessageService messageService) {
        this.credentials = credentials;
        this.web3j = web3j;
        this.transactionManager = transactionManager;
        this.messageService = messageService;
    }

    public ContractGasProvider getActualGasProvider() throws IOException {
        BigInteger gasPrice = BlockchainUtils.getGasPrice(web3j);
        return new StaticGasProvider(gasPrice, DefaultGasProvider.GAS_LIMIT);
    }

    public ContractGasProvider getActualGasProvider(BigInteger gasLimit) throws IOException {
        BigInteger gasPrice = BlockchainUtils.getGasPrice(web3j);
        return new StaticGasProvider(gasPrice, gasLimit);
    }


    private AbstractContract loadContract(br.com.elysian.tartarus.model.Contract contract, ContractGasProvider gasProvider) throws ContractLoadFailed {
        try {
            String clientAddress = transactionManager.getFromAddress();
            String binary = contract.getBin();

            return new AbstractContract(binary, clientAddress, web3j, transactionManager, gasProvider);

        } catch (Exception e) {

            throw new ContractLoadFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        }
    }

    public AbstractContract loadContract(br.com.elysian.tartarus.model.Contract contract) throws ContractLoadFailed, IOException {
        return this.loadContract(contract, getActualGasProvider());
    }

    public <T extends Contract> T deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz) throws Exception {

        // Prepare uma transação para o deploy
        String binary = contract.getBin();
        String fromAddress = credentials.getAddress();
        BigInteger nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameter.valueOf("latest")).send().getTransactionCount();
        BigInteger gasPrice = BlockchainUtils.getGasPrice(web3j);
        BigInteger value = BigInteger.ZERO; // Normalmente, zero para deploy
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;

        // Crie a transação para estimar o gás necessário para o deploy
        Transaction transaction = Transaction.createContractTransaction(
                fromAddress, nonce, gasPrice, gasLimit, value, binary);

        BigInteger estimateGas = BlockchainUtils.getEstimatedGasPrice(web3j, transaction);

        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, estimateGas.multiply(new BigInteger("3")));

        AbstractContract abstractContract = this.loadContract(contract, gasProvider);

        return abstractContract.deploy(clazz, estimateGas).send();
    }

    public AbstractContract deploy(br.com.elysian.tartarus.model.Contract contract) throws Exception {
        return this.deploy(contract, AbstractContract.class);
    }

}
