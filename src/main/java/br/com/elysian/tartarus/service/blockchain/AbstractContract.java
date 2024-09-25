package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.dto.v1.blockchain.ParameterDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.RequestSpecDTO;
import br.com.elysian.tartarus.utils.BlockchainUtils;
import br.com.elysian.tartarus.utils.Web3jTypeMapper;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticEIP1559GasProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

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


    public Function buildFunction(String name, ParameterDTO[] params) {
//        if (outType != null) {
//            TypeReference<?> outRef = Web3jTypeMapper.getOutputTypeReference(outType.name());
//        }

        return new Function(name,
                Arrays.stream(params)
                        .map((param) -> {
                            try {
                                return Web3jTypeMapper.convertToWeb3Type(param.getType(), param.getValue());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }).toList(),
//                List.of(outRef)
                Collections.emptyList()
        );
    }


    public StaticEIP1559GasProvider estimateFunctionGas(Function function) throws IOException {
        return BlockchainUtils.getGasProvider(web3j, transactionManager, contractAddress, FunctionEncoder.encode(function));
    }

    public StaticEIP1559GasProvider estimateFunctionGas(Function function, RequestSpecDTO requestSpec) throws IOException {
        return BlockchainUtils.getGasProvider(web3j, transactionManager, contractAddress, FunctionEncoder.encode(function), requestSpec);
    }


    public RemoteFunctionCall<TransactionReceipt> executeFunction(Function function, ContractGasProvider gasProvider) {
        if (gasProvider != null) {
            this.setGasProvider(gasProvider);
        }
//        return executeRemoteCallSingleValueReturn(function, outRef.getClassType());
        return executeRemoteCallTransaction(function);
    }

}
