package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.dto.v1.blockchain.ParameterDTO;
import br.com.elysian.tartarus.utils.ParameterUtils;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class AbstractContract extends Contract {

    public AbstractContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super("", contractAddress, web3j, transactionManager, gasProvider);
    }

    public AbstractContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    public AbstractContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super("", contractAddress, web3j, credentials, gasProvider);
    }

    public AbstractContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    public AbstractContract(EnsResolver ensResolver, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(ensResolver, "", contractAddress, web3j, transactionManager, gasProvider);
    }

    public AbstractContract(EnsResolver ensResolver, String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(ensResolver, contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }


    public static AbstractContract fromContract(Contract contract, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        return new AbstractContract(contract.getContractBinary(), contract.getContractAddress(), web3j, transactionManager, gasProvider);
    }

    public static AbstractContract fromContract(Contract contract, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return new AbstractContract(contract.getContractBinary(), contract.getContractAddress(), web3j, credentials, gasProvider);
    }

//    public RemoteFunctionCall<T> executeFunc(String name, ParameterDTO[] inParams, ParameterDTO[] outParams) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        final Function function = new Function(name,
//                ParameterUtils.castAndList(inParams),
//                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
//        return executeRemoteCallSingleValueReturn(function, String.class);
//    }

}
