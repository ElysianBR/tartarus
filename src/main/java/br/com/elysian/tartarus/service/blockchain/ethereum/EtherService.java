package br.com.elysian.tartarus.service.blockchain.ethereum;

import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.service.blockchain.NetworkClient;
import br.com.elysian.tartarus.service.blockchain.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

@Service
public class EtherService {

    private final NetworkService networkService;


    @Autowired
    public EtherService(NetworkService networkService) {
        this.networkService = networkService;
    }


    public NetworkClient getNetworkClient(CompanyNetworkAccount companyNetworkAccount) {

        String url = companyNetworkAccount.getCompanyNetwork().getNetwork().getNetworkUrl();
        Web3j web3j = Web3j.build(new HttpService(url));
        BigInteger maxGasWei = companyNetworkAccount.getCompanyNetwork().getMaxGasWei();


        String clientAdress = companyNetworkAccount.getAddress();
        TransactionManager transactionManager = new ClientTransactionManager(web3j, clientAdress);


        return new NetworkClient(web3j, transactionManager, maxGasWei);
    }


    public NetworkClient getNetworkClient() throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException {
        return this.getNetworkClient(networkService.getCompanyNetworkAccountLoggedIn());
    }


}
