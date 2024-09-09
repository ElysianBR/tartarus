package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.exception.ContractLoadFailed;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.utils.BlockchainUtils;
import br.com.elysian.tartarus.utils.InfuraUtils;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

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


    private AbstractContract loadContract(CompanyNetworkContract contract, ContractGasProvider gasProvider) throws ContractLoadFailed {
        try {
            String contractAddress = contract.getContractAddress();
            String binary = contract.getContract().getBin();

            return new AbstractContract(binary, contractAddress, web3j, transactionManager, gasProvider);

        } catch (Exception e) {

            throw new ContractLoadFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        }
    }

    public AbstractContract loadContract(CompanyNetworkContract contract) throws ContractLoadFailed {
        return this.loadContract(contract, new DefaultGasProvider());
    }

    public <T extends Contract> CompletableFuture<T> deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz) throws Exception {
        String binary = contract.getBin();
        String fromAddress = credentials.getAddress();

        BigInteger gasPrice = BlockchainUtils.getGasPrice(web3j);
        BigInteger estimateGas = BlockchainUtils.getEstimatedGasPriceToDeploy(web3j, gasPrice, fromAddress, binary);

        System.out.println("gasPrice: " + gasPrice);
        System.out.println("estimateGas: " + estimateGas);

        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, estimateGas);

        return Contract.deployRemoteCall(
                clazz, web3j, transactionManager, gasProvider, binary, ""
        ).sendAsync();
    }

    public CompletableFuture<AbstractContract> deploy(br.com.elysian.tartarus.model.Contract contract) throws Exception {
        return this.deploy(contract, AbstractContract.class);
    }

}
