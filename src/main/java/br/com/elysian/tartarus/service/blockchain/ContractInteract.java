package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.service.blockchain.model.owner.Owner;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.util.function.Consumer;

public class ContractInteract<T extends Contract> {

    private final T contract;

    public ContractInteract(T contract) {
        this.contract = contract;
    }

    public void executeTxt(Consumer<T> c) {
            c.accept(contract);
    }

}
