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
import java.util.concurrent.CompletableFuture;

public class ContractClient {

    private static final BigInteger DEFAULT_MAX_PRIORITY_FEE_PER_GAS = Convert.toWei("1.5", Convert.Unit.GWEI).toBigInteger(); // 2 Gwei

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
            String binary = contract.getPk().getContract().getBin();

            return new AbstractContract(binary, contractAddress, web3j, transactionManager, gasProvider);

        } catch (Exception e) {

            throw new ContractLoadFailed(messageService.getMessage("tartarus.blockchain.ethereum.unable.create.contract.instance"), e);

        }
    }

    public AbstractContract loadContract(CompanyNetworkContract contract) throws ContractLoadFailed {
        return this.loadContract(contract, new DefaultGasProvider());
    }


    public StaticEIP1559GasProvider getEIP1559GasProvider(BigInteger maxFeePerGas, BigInteger maxPriorityFeePerGas, BigInteger estimateGas) throws IOException {
        long chainId = web3j.ethChainId().send().getChainId().longValue();
        return new StaticEIP1559GasProvider(chainId, maxFeePerGas, maxPriorityFeePerGas, estimateGas);
    }


    public StaticEIP1559GasProvider getEIP1559GasProvider(BigInteger maxFeePerGas, BigInteger estimateGas) throws IOException {
        return this.getEIP1559GasProvider(maxFeePerGas, DEFAULT_MAX_PRIORITY_FEE_PER_GAS, estimateGas);
    }


    public StaticEIP1559GasProvider getDeployGasProvider(String binary) throws IOException {
        String fromAddress = transactionManager.getFromAddress();
        BigInteger baseFee = BlockchainUtils.getGasPrice(web3j);
        BigInteger maxFeePerGas = baseFee.add(DEFAULT_MAX_PRIORITY_FEE_PER_GAS);
        BigInteger estimateGas = BlockchainUtils.getEstimatedGasPriceToDeploy(web3j, baseFee, fromAddress, binary);
        return getEIP1559GasProvider(maxFeePerGas, estimateGas);
    }


    public StaticEIP1559GasProvider getDeployGasProvider(String binary, RequestSpecDTO specDTO) throws IOException {
        if (specDTO.getGasPriceWei() != null || specDTO.getMaxPriorityFeePerGasGwei() != null || specDTO.getGasLimitWei() != null) {
            BigInteger baseFee;
            BigInteger maxPriorityFeePerGas;
            BigInteger gasLimit;

            if (specDTO.getGasPriceWei() != null) {
                baseFee = specDTO.getGasPriceWei();
            } else {
                baseFee = BlockchainUtils.getBaseFee(web3j);
            }

            if (specDTO.getMaxPriorityFeePerGasGwei() != null) {
                maxPriorityFeePerGas = specDTO.getMaxPriorityFeePerGasGwei();
            } else {
                maxPriorityFeePerGas = DEFAULT_MAX_PRIORITY_FEE_PER_GAS;
            }

            if (specDTO.getGasLimitWei() != null) {
                gasLimit = specDTO.getGasLimitWei();
            } else {
                String fromAddress = transactionManager.getFromAddress();
                gasLimit = BlockchainUtils.getEstimatedGasPriceToDeploy(web3j, baseFee, fromAddress, binary);
            }

            BigInteger maxFeePerGas = baseFee.add(maxPriorityFeePerGas);

            return getEIP1559GasProvider(maxFeePerGas, maxPriorityFeePerGas, gasLimit);
        }
        return getDeployGasProvider(binary);
    }


    public <T extends Contract> RemoteCall<T> deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz, ContractGasProvider gasProvider) throws Exception {
        String binary = contract.getBin();
        return Contract.deployRemoteCall(
                clazz, web3j, transactionManager, gasProvider, binary, ""
        );
    }

    public <T extends Contract> RemoteCall<T> deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz) throws Exception {
        String binary = contract.getBin();
        ContractGasProvider gasProvider = getDeployGasProvider(binary);
        return this.deploy(contract, clazz, gasProvider);
    }

    public <T extends Contract> RemoteCall<T> deploy(br.com.elysian.tartarus.model.Contract contract, Class<T> clazz, RequestSpecDTO specDTO) throws Exception {
        String binary = contract.getBin();
        ContractGasProvider gasProvider = getDeployGasProvider(binary, specDTO);
        return this.deploy(contract, clazz, gasProvider);
    }

    public RemoteCall<AbstractContract> deploy(br.com.elysian.tartarus.model.Contract contract) throws Exception {
        return this.deploy(contract, AbstractContract.class);
    }

    public  RemoteCall<AbstractContract> deploy(br.com.elysian.tartarus.model.Contract contract, RequestSpecDTO specDTO) throws Exception {
        return this.deploy(contract, AbstractContract.class, specDTO);
    }

}
