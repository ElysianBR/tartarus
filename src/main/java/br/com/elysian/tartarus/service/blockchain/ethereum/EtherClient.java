package br.com.elysian.tartarus.service.blockchain.ethereum;

import br.com.elysian.tartarus.exception.BlockchainNotFoundException;
import br.com.elysian.tartarus.exception.BlockchainNotLoggedInException;
import br.com.elysian.tartarus.exception.CompanyNotLoggedInException;
import br.com.elysian.tartarus.model.CompanyBlockchain;
import br.com.elysian.tartarus.service.blockchain.BlockchainService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

@Getter
@Service
public class EtherClient {


    private static final Logger LOGGER = LoggerFactory.getLogger(EtherClient.class);
    private @Value("#{messageService.getMessage('tartarus.blockchain.ethereum.unable.create.contract.instance')}") String messageFail;


    private final BlockchainService blockchainService;


    private CompanyBlockchain companyBlockchain;
    private String url;
    private String clientAdress;
    private BigInteger maxGasWei;
    private Web3j web3j;


    @Autowired
    public EtherClient(BlockchainService blockchainService) throws CompanyNotLoggedInException, BlockchainNotLoggedInException, BlockchainNotFoundException {
        this.blockchainService = blockchainService;
        CompanyBlockchain companyBlockchain = blockchainService.getBlockchainLoggedIn();
        this.setCompanyBlockchain(companyBlockchain);
    }


    public void setCompanyBlockchain(CompanyBlockchain companyBlockchain) {
        this.companyBlockchain = companyBlockchain;

        this.url = companyBlockchain.getBlockchain().getNetworkUrl();
        this.web3j = Web3j.build(new HttpService(this.url));

        this.clientAdress = companyBlockchain.getAdress();
        this.maxGasWei = companyBlockchain.getMaxGasWei();
    }


    public <T extends Contract> T getContract(Class<T> clazz) throws ContractInstantiationFailed {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, Web3j.class, TransactionManager.class, ContractGasProvider.class);
            constructor.setAccessible(true);
            return constructor.newInstance(contractAddress, web3j, transactionManager, gasProvider);
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            LOGGER.error(messageFail, e);
            throw new ContractInstantiationFailed(messageFail, e);
        }
    }


}
