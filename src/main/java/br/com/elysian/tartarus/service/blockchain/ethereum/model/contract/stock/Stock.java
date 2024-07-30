package br.com.elysian.tartarus.service.blockchain.ethereum.model.contract.stock;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
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
public class Stock extends Contract {
    public static final String BINARY = "608080604052346026575f80546001600160a01b031916331790556106a5908161002b8239f35b5f80fdfe6080806040526004361015610012575f80fd5b5f3560e01c90816307546172146104295750806320cd3a3614610395578063a2f4b0de146100935763cde7d99b14610048575f80fd5b3461008f57602036600319011261008f5760043567ffffffffffffffff811161008f576040600319823603011261008f5761008760209160040161051a565b604051908152f35b5f80fd5b3461008f57602036600319011261008f5760043567ffffffffffffffff811161008f57806004016080600319833603011261008f575f546001600160a01b0316330361008f57335f52600160205260405f205f916100f1818061044b565b6040519063cde7d99b60e01b825260206004830152803560248301526020810135601e198236030181121561008f570160208135910167ffffffffffffffff821161008f57813603811361008f5760848383602094829460406044850152816064850152848401375f828201840152601f01601f19168101030181305afa5f9181610361575b5061035857505f5b156101ba575060049161019191610460565b50016064815492013582018092116101a65755005b634e487b7160e01b5f52601160045260245ffd5b929150805468010000000000000000811015610331576101df91600182018155610460565b92909261034557806101f09161044b565b80358355610206600184019160208101906104af565b9067ffffffffffffffff82116103315761022083546104e2565b601f81116102ec575b505f90601f831160011461028257918060649492600496945f92610277575b50508160011b915f199060031b1c19161790555b60248101356002850155604481013560038501550135910155005b013590508780610248565b601f19831691845f5260205f20925f5b8181106102d457509260019285926004989660649896106102bb575b505050811b01905561025c565b01355f19600384901b60f8161c191690558780806102ae565b91936020600181928787013581550195019201610292565b835f5260205f20601f840160051c81019160208510610327575b601f0160051c01905b81811061031c5750610229565b5f815560010161030f565b9091508190610306565b634e487b7160e01b5f52604160045260245ffd5b634e487b7160e01b5f525f60045260245ffd5b9250600161017f565b9091506020813d60201161038d575b8161037d6020938361048d565b8101031261008f57519086610177565b3d9150610370565b3461008f57602036600319011261008f5760043567ffffffffffffffff811161008f578060040160a0600319833603011261008f5760846103f6600492335f5260016020526103f06103eb60405f20928061044b565b61051a565b90610460565b5092013591018054918281116104125782039182116101a65755005b9050630e94343d60e01b5f5260045260245260445ffd5b3461008f575f36600319011261008f575f546001600160a01b03168152602090f35b903590603e198136030182121561008f570190565b8054821015610479575f52600560205f20910201905f90565b634e487b7160e01b5f52603260045260245ffd5b90601f8019910116810190811067ffffffffffffffff82111761033157604052565b903590601e198136030182121561008f570180359067ffffffffffffffff821161008f5760200191813603831361008f57565b90600182811c92168015610510575b60208310146104fc57565b634e487b7160e01b5f52602260045260245ffd5b91607f16916104f1565b335f52600160205260405f20905f918054925b83811061054357630aa9257360e31b5f5260045ffd5b61054d8183610460565b508054843514908161056e575b506105675760010161052d565b9250505090565b60019150016040515f9181815493610585856104e2565b9283835260208301956001811690815f14610650575060011461060e575b506105b09250038261048d565b5190206105c060208501856104af565b67ffffffffffffffff811161033157604051906105e7601f8201601f19166020018361048d565b8082526020820192368282011161008f57815f92602092863783010152519020145f61055a565b9150505f528160205f20915f925b81841061063457505060206105b0928201015f6105a3565b602091925080600191548386880101520192019190839161061c565b60ff19168752506105b093151560051b830160200191505f90506105a356fea2646970667358221220f7f04b4d1125f9b5a710332bd438f738e27e31f37ed9630a7b2bd35dd29eaecf64736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_FINDSTOCKINDEX = "findStockIndex";

    public static final String FUNC_MINTSTOCK = "mintStock";

    public static final String FUNC_MINTER = "minter";

    public static final String FUNC_MOVIMENTSTOCK = "movimentStock";

    @Deprecated
    protected Stock(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Stock(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Stock(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Stock(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> findStockIndex(StockPksDef pks) {
        final Function function = new Function(FUNC_FINDSTOCKINDEX, 
                Arrays.<Type>asList(pks), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mintStock(StockDef stockMint) {
        final Function function = new Function(
                FUNC_MINTSTOCK, 
                Arrays.<Type>asList(stockMint), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> minter() {
        final Function function = new Function(FUNC_MINTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> movimentStock(MovimentDef moviment) {
        final Function function = new Function(
                FUNC_MOVIMENTSTOCK, 
                Arrays.<Type>asList(moviment), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Stock load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Stock(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Stock load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Stock(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Stock load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Stock(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Stock load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Stock(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Stock> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Stock.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<Stock> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Stock.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Stock> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Stock.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Stock> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Stock.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class StockPksDef extends DynamicStruct {
        public BigInteger product_id;

        public String control;

        public StockPksDef(BigInteger product_id, String control) {
            super(new org.web3j.abi.datatypes.generated.Uint256(product_id), 
                    new org.web3j.abi.datatypes.Utf8String(control));
            this.product_id = product_id;
            this.control = control;
        }

        public StockPksDef(Uint256 product_id, Utf8String control) {
            super(product_id, control);
            this.product_id = product_id.getValue();
            this.control = control.getValue();
        }
    }

    public static class StockDef extends DynamicStruct {
        public StockPksDef pks;

        public BigInteger manufacturing_timestamp;

        public BigInteger validity_timestamp;

        public BigInteger quantity;

        public StockDef(StockPksDef pks, BigInteger manufacturing_timestamp,
                BigInteger validity_timestamp, BigInteger quantity) {
            super(pks, 
                    new org.web3j.abi.datatypes.generated.Uint256(manufacturing_timestamp), 
                    new org.web3j.abi.datatypes.generated.Uint256(validity_timestamp), 
                    new org.web3j.abi.datatypes.generated.Uint256(quantity));
            this.pks = pks;
            this.manufacturing_timestamp = manufacturing_timestamp;
            this.validity_timestamp = validity_timestamp;
            this.quantity = quantity;
        }

        public StockDef(StockPksDef pks, Uint256 manufacturing_timestamp,
                Uint256 validity_timestamp, Uint256 quantity) {
            super(pks, manufacturing_timestamp, validity_timestamp, quantity);
            this.pks = pks;
            this.manufacturing_timestamp = manufacturing_timestamp.getValue();
            this.validity_timestamp = validity_timestamp.getValue();
            this.quantity = quantity.getValue();
        }
    }

    public static class MovimentDef extends DynamicStruct {
        public StockPksDef pks;

        public String recipient;

        public BigInteger manufacturing_timestamp;

        public BigInteger validity_timestamp;

        public BigInteger quantity;

        public MovimentDef(StockPksDef pks, String recipient, BigInteger manufacturing_timestamp,
                BigInteger validity_timestamp, BigInteger quantity) {
            super(pks, 
                    new org.web3j.abi.datatypes.Address(160, recipient), 
                    new org.web3j.abi.datatypes.generated.Uint256(manufacturing_timestamp), 
                    new org.web3j.abi.datatypes.generated.Uint256(validity_timestamp), 
                    new org.web3j.abi.datatypes.generated.Uint256(quantity));
            this.pks = pks;
            this.recipient = recipient;
            this.manufacturing_timestamp = manufacturing_timestamp;
            this.validity_timestamp = validity_timestamp;
            this.quantity = quantity;
        }

        public MovimentDef(StockPksDef pks, Address recipient, Uint256 manufacturing_timestamp,
                Uint256 validity_timestamp, Uint256 quantity) {
            super(pks, recipient, manufacturing_timestamp, validity_timestamp, quantity);
            this.pks = pks;
            this.recipient = recipient.getValue();
            this.manufacturing_timestamp = manufacturing_timestamp.getValue();
            this.validity_timestamp = validity_timestamp.getValue();
            this.quantity = quantity.getValue();
        }
    }
}
