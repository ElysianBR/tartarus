package br.com.elysian.tartarus.service.blockchain;


import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetworkAccount;
import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.model.Contract;
import br.com.elysian.tartarus.model.pk.CompanyNetworkContractPK;
import br.com.elysian.tartarus.model.pk.ContractPK;
import br.com.elysian.tartarus.repository.CompanyNetworkContractDAO;
import br.com.elysian.tartarus.repository.ContractDAO;
import br.com.elysian.tartarus.service.MessageService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class ContractService {


    private final MessageService messageService;
    private final NetworkService networkService;


    private final ContractDAO contractDAO;
    private final CompanyNetworkContractDAO companyNetworkContractDAO;


    @Autowired
    public ContractService(MessageService messageService, NetworkService networkService,
                           ContractDAO contractDAO, CompanyNetworkContractDAO companyNetworkContractDAO) {
        this.messageService = messageService;
        this.networkService = networkService;
        this.contractDAO = contractDAO;
        this.companyNetworkContractDAO = companyNetworkContractDAO;
    }


    public Contract getContract(String contractName, String version) throws ContractNotFoundException {
        ContractPK contractPK = new ContractPK(contractName, version);
        Optional<Contract> contractOpt = contractDAO.findById(contractPK);

        if (contractOpt.isPresent()) {
            return contractOpt.get();
        }

        throw new ContractNotFoundException(String.format(messageService.getMessage("tartarus.blockchain.contract.not-found"), contractName, version));
    }


    public Iterable<Contract> getContractsAvailable() {
        return contractDAO.findAll();
    }


    public CompanyNetworkContract getNetworkContract(String contractName, String version) throws ContractNotFoundException, NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, NetworkContractNotRegistered {
        Contract contract = this.getContract(contractName, version);

        CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();

        CompanyNetworkContractPK companyNetworkContractPK = new CompanyNetworkContractPK(companyNetwork, contract);

        Optional<CompanyNetworkContract> companyNetworkContract = companyNetworkContractDAO.findById(companyNetworkContractPK);
        if (companyNetworkContract.isPresent()) {
            return companyNetworkContract.get();
        }


        throw new NetworkContractNotRegistered(String.format(messageService.getMessage("tartarus.blockchain.contract.not-registered-in-company"), contractName, version, companyNetwork.getNetwork().getNetworkNameFormated()));
    }

    private ContractClient getContractClient(Credentials credentials, CompanyNetworkAccount companyNetworkAccount) throws IOException {
        String url = companyNetworkAccount.getCompanyNetwork().getNetwork().getNetworkUrl();
        OkHttpClient.Builder connBuilder = new OkHttpClient.Builder();
        connBuilder.callTimeout(5, TimeUnit.MINUTES);
        connBuilder.connectTimeout(5, TimeUnit.MINUTES);
        connBuilder.readTimeout(5, TimeUnit.MINUTES);
        connBuilder.writeTimeout(5, TimeUnit.MINUTES);
        Web3j web3j = Web3j.build(new HttpService(url, new OkHttpClient(connBuilder)));
//        Web3j web3j = Web3j.build(new HttpService(url));

        TransactionManager transactionManager;
//        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, 10, 5);

        if (credentials == null) {
            String clientAddress = companyNetworkAccount.getAddress();
            transactionManager = new ClientTransactionManager(web3j, clientAddress);
        } else {
            long chainId = web3j.ethChainId().send().getChainId().longValue();
            transactionManager = new RawTransactionManager(web3j, credentials, chainId);
        }

        return new ContractClient(credentials, web3j, transactionManager, messageService);
    }

    public ContractClient getContractClient(String password, CompanyNetworkAccount companyNetworkAccount) throws NetworkNotLoggedInException, InvalidUsernameOrPasswordException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NetworkNotFoundException, PrivateKeyNotSet, IOException {
        Credentials credentials = null;
        if (password != null) {
            String privateKey = networkService.getPrivateKey(password);
            credentials = Credentials.create(privateKey);
        }
        return this.getContractClient(credentials, companyNetworkAccount);
    }

    public ContractClient getContractClient(String password) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, InvalidUsernameOrPasswordException, PrivateKeyNotSet, IOException {
        return this.getContractClient(password, networkService.getAccountCompanyNetworkLoggedIn());
    }

    public ContractClient getContractClient(CompanyNetworkAccount companyNetworkAccount) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.getContractClient((Credentials) null, companyNetworkAccount);
    }

    public ContractClient getContractClient() throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.getContractClient(networkService.getAccountCompanyNetworkLoggedIn());
    }


    public <T extends org.web3j.tx.Contract> CompanyNetworkContract saveDeployInDB(T deployOut, Contract contract) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, ContractNotFoundException {
        CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();
        String contractAddress = deployOut.getContractAddress();

        CompanyNetworkContract companyNetworkContract = new CompanyNetworkContract(new CompanyNetworkContractPK(companyNetwork, contract), BigInteger.ZERO, contractAddress);
        return companyNetworkContractDAO.save(companyNetworkContract);
    }

    public <T extends org.web3j.tx.Contract> CompanyNetworkContract saveDeployInDB(T deployOut, String contractName, String version) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, ContractNotFoundException {
        Contract contract = getContract(contractName, version);
        return saveDeployInDB(deployOut, contract);
    }

}
