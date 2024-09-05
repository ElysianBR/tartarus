package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.InventoryManagement;
import org.web3j.crypto.Credentials;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

public class AbstractContract extends Contract {


    public AbstractContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    public AbstractContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    public AbstractContract(EnsResolver ensResolver, String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(ensResolver, contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    public <T extends Contract> RemoteCall<T> deploy(Class<T> clazz) {
        return deployRemoteCall(clazz, super.web3j, super.transactionManager, super.gasProvider, super.contractBinary, "");
    }


}
