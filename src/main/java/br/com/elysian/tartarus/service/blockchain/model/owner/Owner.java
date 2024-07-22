package br.com.elysian.tartarus.service.blockchain.model.owner;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.0.
 */
@SuppressWarnings("rawtypes")
public class Owner extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b506100556040518060400160405280601b81526020017f4f776e657220636f6e7472616374206465706c6f7965642062793a00000000008152503361011260201b60201c565b335f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff165f73ffffffffffffffffffffffffffffffffffffffff167f342827c97908e5e2f71151c08502a66d44b6f758e3ac2f1de95f02eb95f0a73560405160405180910390a361031d565b6101b082826040516024016101289291906102c2565b6040516020818303038152906040527f319af333000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506101b460201b60201c565b5050565b6101d5816101d06101d860201b6101e1176101f760201b60201c565b60201c565b50565b5f6a636f6e736f6c652e6c6f6790505f80835160208501845afa505050565b61020960201b61020017819050919050565b6102116102f0565b565b5f81519050919050565b5f82825260208201905092915050565b8281835e5f83830152505050565b5f601f19601f8301169050919050565b5f61025582610213565b61025f818561021d565b935061026f81856020860161022d565b6102788161023b565b840191505092915050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f6102ac82610283565b9050919050565b6102bc816102a2565b82525050565b5f6040820190508181035f8301526102da818561024b565b90506102e960208301846102b3565b9392505050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52605160045260245ffd5b6103968061032a5f395ff3fe608060405234801561000f575f80fd5b5060043610610034575f3560e01c8063893d20e814610038578063a6f9dae114610056575b5f80fd5b610040610072565b60405161004d9190610249565b60405180910390f35b610070600480360381019061006b9190610290565b610099565b005b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b5f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610126576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161011d90610315565b60405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff165f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f342827c97908e5e2f71151c08502a66d44b6f758e3ac2f1de95f02eb95f0a73560405160405180910390a3805f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b5f6a636f6e736f6c652e6c6f6790505f80835160208501845afa505050565b610208610333565b565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f6102338261020a565b9050919050565b61024381610229565b82525050565b5f60208201905061025c5f83018461023a565b92915050565b5f80fd5b61026f81610229565b8114610279575f80fd5b50565b5f8135905061028a81610266565b92915050565b5f602082840312156102a5576102a4610262565b5b5f6102b28482850161027c565b91505092915050565b5f82825260208201905092915050565b7f43616c6c6572206973206e6f74206f776e6572000000000000000000000000005f82015250565b5f6102ff6013836102bb565b915061030a826102cb565b602082019050919050565b5f6020820190508181035f83015261032c816102f3565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52605160045260245ffdfea26469706673582212208467afb41d0e3cd2f0b4a8885f9959da73164e6aa9812f951aa3bc71abc9b9ad64736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CHANGEOWNER = "changeOwner";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final Event OWNERSET_EVENT = new Event("OwnerSet", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Owner(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Owner(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Owner(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Owner(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> changeOwner(String newOwner) {
        final Function function = new Function(
                FUNC_CHANGEOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<OwnerSetEventResponse> getOwnerSetEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSET_EVENT, transactionReceipt);
        ArrayList<OwnerSetEventResponse> responses = new ArrayList<OwnerSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnerSetEventResponse typedResponse = new OwnerSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnerSetEventResponse getOwnerSetEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSET_EVENT, log);
        OwnerSetEventResponse typedResponse = new OwnerSetEventResponse();
        typedResponse.log = log;
        typedResponse.oldOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnerSetEventResponse> ownerSetEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnerSetEventFromLog(log));
    }

    public Flowable<OwnerSetEventResponse> ownerSetEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSET_EVENT));
        return ownerSetEventFlowable(filter);
    }

    public RemoteFunctionCall<String> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static Owner load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Owner(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Owner load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Owner(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Owner load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Owner(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Owner load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Owner(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Owner> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Owner.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<Owner> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Owner.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Owner> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Owner.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Owner> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Owner.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class OwnerSetEventResponse extends BaseEventResponse {
        public String oldOwner;

        public String newOwner;
    }
}
