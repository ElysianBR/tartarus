package br.com.elysian.tartarus.service.blockchain.ethereum;

import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.service.blockchain.NetworkClient;
import br.com.elysian.tartarus.service.blockchain.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class EtherService {

    private final NetworkService networkService;
    private final MessageService messageService;


    @Autowired
    public EtherService(NetworkService networkService, MessageService messageService) {
        this.networkService = networkService;
        this.messageService = messageService;
    }


    public NetworkClient getNetworkClient(String password, AccountCompanyNetwork accountCompanyNetwork) throws NetworkNotLoggedInException, InvalidUsernameOrPasswordException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NetworkNotFoundException, PrivateKeyNotSet, IOException {
        String privateKey = networkService.getPrivateKey(password);
        Credentials credentials = Credentials.create(privateKey);
        return this.getNetworkClient(credentials, accountCompanyNetwork);
    }


    public NetworkClient getNetworkClient(AccountCompanyNetwork accountCompanyNetwork) throws IOException {
        return this.getNetworkClient((Credentials) null, accountCompanyNetwork);
    }


    public NetworkClient getNetworkClient(String password) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException {
        return this.getNetworkClient(password, networkService.getAccountCompanyNetworkLoggedIn());
    }


    public NetworkClient getNetworkClient() throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, IOException {
        return this.getNetworkClient(networkService.getAccountCompanyNetworkLoggedIn());
    }


    private NetworkClient getNetworkClient(Credentials credentials, AccountCompanyNetwork accountCompanyNetwork) throws IOException {

        String url = accountCompanyNetwork.getCompanyNetwork().getNetwork().getNetworkUrl();
        Web3j web3j = Web3j.build(new HttpService(url));
        BigInteger maxGasWei = accountCompanyNetwork.getCompanyNetwork().getMaxGasWei();


        String clientAddress = accountCompanyNetwork.getAddress();
        TransactionManager transactionManager;

//        PollingTransactionReceiptProcessor poll = new PollingTransactionReceiptProcessor(web3j, 120000, 40);

        if (credentials == null) {
            transactionManager = new ClientTransactionManager(web3j, clientAddress);
        } else {
            long chainId = web3j.ethChainId().send().getChainId().longValue();
            transactionManager = new RawTransactionManager(web3j, credentials, chainId);
        }

        return new NetworkClient(credentials, web3j, transactionManager, maxGasWei, messageService);
    }


}
