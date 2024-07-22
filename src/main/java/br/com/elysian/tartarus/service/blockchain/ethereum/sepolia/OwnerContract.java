package br.com.elysian.tartarus.service.blockchain.ethereum.sepolia;

import br.com.elysian.tartarus.service.blockchain.model.owner.Owner;
import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class OwnerContract {

    private final Web3j web3j;
    private static final String contractAddress = "0xd8b934580fcE35a11B58C6D73aDeE468a2833fa8";
    private static final BigInteger gasPrice = BigInteger.valueOf(20000000000L);
    private static final BigInteger gasLimit = BigInteger.valueOf(6721975L);

    private final TransactionManager transactionManager;
    private final ContractGasProvider gasProvider;

    @Getter
    private final Owner owner;

    public OwnerContract(Web3j web3j) {
        this.web3j = web3j;

        // TransactionManager gerencia as transações
        this.transactionManager = new ClientTransactionManager(web3j, "0x5B38Da6a701c568545dCfcB03FcB875f56beddC4");
        // Gas Provider
        this.gasProvider = new StaticGasProvider(gasPrice, gasLimit);

        // Carregar o contrato
        this.owner = Owner.load(contractAddress, web3j, transactionManager, gasProvider);
    }

}
