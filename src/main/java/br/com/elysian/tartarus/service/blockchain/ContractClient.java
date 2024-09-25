package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.dto.v1.blockchain.RequestSpecDTO;
import br.com.elysian.tartarus.exception.ContractLoadFailed;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.utils.BlockchainUtils;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticEIP1559GasProvider;
import org.web3j.utils.Convert;

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


    public <T extends Contract> RemoteCall<T> deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz, ContractGasProvider gasProvider) throws Exception {
        String binary = contract.getBin();
        return Contract.deployRemoteCall(
                clazz, web3j, transactionManager, gasProvider, binary, ""
        );
    }

    public <T extends Contract> RemoteCall<T> deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz) throws Exception {
        String binary = contract.getBin();
        ContractGasProvider gasProvider = BlockchainUtils.getGasProvider(web3j, transactionManager, binary);
        return this.deploy(contract, clazz, gasProvider);
    }

    public <T extends Contract> RemoteCall<T> deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz, RequestSpecDTO specDTO) throws Exception {
        String binary = contract.getBin();
        ContractGasProvider gasProvider = BlockchainUtils.getGasProvider(web3j, transactionManager, binary, specDTO);
        return this.deploy(contract, clazz, gasProvider);
    }

    public RemoteCall<AbstractContract> deploy(br.com.elysian.tartarus.model.Contract contract) throws Exception {
        return this.deploy(contract, AbstractContract.class);
    }

    public RemoteCall<AbstractContract> deploy(br.com.elysian.tartarus.model.Contract contract, RequestSpecDTO specDTO) throws Exception {
        return this.deploy(contract, AbstractContract.class, specDTO);
    }


    public AbstractContract loadContract(String contractAddress, String binary, ContractGasProvider gasProvider) throws ContractLoadFailed {
        try {
            return new AbstractContract(binary, contractAddress, web3j, transactionManager, gasProvider);
        } catch (Exception e) {
            throw new ContractLoadFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);
        }
    }

    public AbstractContract loadContract(CompanyNetworkContract contract, ContractGasProvider gasProvider) throws ContractLoadFailed {
        String contractAddress = contract.getContractAddress();
        String binary = contract.getPk().getContract().getBin();
        return this.loadContract(contractAddress, binary, gasProvider);
    }


    public AbstractContract loadContract(CompanyNetworkContract contract) throws ContractLoadFailed {
        String contractAddress = contract.getContractAddress();
        String binary = contract.getPk().getContract().getBin();
        return this.loadContract(contractAddress, binary, new DefaultGasProvider());
    }


}
