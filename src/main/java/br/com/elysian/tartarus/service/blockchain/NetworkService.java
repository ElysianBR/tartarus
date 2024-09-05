package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.SuccessDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.NetworkDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.SavePrivateKeyDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.model.pk.CompanyNetworkAccountPK;
import br.com.elysian.tartarus.model.pk.CompanyNetworkPK;
import br.com.elysian.tartarus.repository.*;
import br.com.elysian.tartarus.service.AccountService;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.CompanyService;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.utils.BlockchainUtils;
import br.com.elysian.tartarus.utils.InfuraUtils;
import br.com.elysian.tartarus.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class NetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    private final AuthService authService;
    private final AccountService accountService;
    private final CompanyService companyService;
    private final NetworkDAO networkDAO;
    private final CompanyNetworkDAO companyNetworkDAO;
    private final MessageService messageService;
    final AccountCompanyNetworkDAO accountCompanyNetworkDAO;


    @Autowired
    public NetworkService(AuthService authService, AccountService accountService, CompanyService companyService, NetworkDAO networkDAO,
                          CompanyNetworkDAO companyNetworkDAO, MessageService messageService, AccountCompanyNetworkDAO accountCompanyNetworkDAO) {
        this.authService = authService;
        this.accountService = accountService;
        this.companyService = companyService;
        this.networkDAO = networkDAO;
        this.companyNetworkDAO = companyNetworkDAO;
        this.messageService = messageService;
        this.accountCompanyNetworkDAO = accountCompanyNetworkDAO;
    }


    private NetworkClient getNetworkClient(Credentials credentials, AccountCompanyNetwork accountCompanyNetwork) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String url = accountCompanyNetwork.getCompanyNetwork().getNetwork().getNetworkUrl();
        Web3j web3j = Web3j.build(new HttpService(url));

        String clientAddress = accountCompanyNetwork.getAddress();
        TransactionManager transactionManager;

        if (credentials == null) {
            transactionManager = new ClientTransactionManager(web3j, clientAddress);
        } else {
            long chainId = web3j.ethChainId().send().getChainId().longValue();
            transactionManager = new RawTransactionManager(web3j, credentials, chainId);
        }

        return new NetworkClient(credentials, web3j, transactionManager, messageService);
    }


    public NetworkClient getNetworkClient(String password, AccountCompanyNetwork accountCompanyNetwork) throws NetworkNotLoggedInException, InvalidUsernameOrPasswordException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NetworkNotFoundException, PrivateKeyNotSet, IOException, KeyStoreException, KeyManagementException {
        String privateKey = this.getPrivateKey(password);
        Credentials credentials = Credentials.create(privateKey);
        return this.getNetworkClient(credentials, accountCompanyNetwork);
    }

    public NetworkClient getNetworkClient(AccountCompanyNetwork accountCompanyNetwork) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.getNetworkClient((Credentials) null, accountCompanyNetwork);
    }

    public NetworkClient getNetworkClient(String password) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, InvalidUsernameOrPasswordException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, PrivateKeyNotSet, IOException, KeyStoreException, KeyManagementException {
        return this.getNetworkClient(password, this.getAccountCompanyNetworkLoggedIn());
    }

    public NetworkClient getNetworkClient() throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.getNetworkClient(this.getAccountCompanyNetworkLoggedIn());
    }



    public void with(String networkTag) throws NetworkNotFoundException {
        AuthenticationAccount authenticationAccount = authService.getCurrentAuthentication();

        if (networkDAO.existsByTag(networkTag)) {
            ((CredentialAccount) authenticationAccount.getCredentials()).setNetworkTag(networkTag);
        } else {
            throw new NetworkNotFoundException(String.format(messageService.getMessage("tartarus.blockchain.network.not-found"), networkTag));
        }
    }


    public Network getNetworkByTag(String networkTag) throws NetworkNotFoundException {
        Optional<Network> network = networkDAO.findByTag(networkTag);

        if (network.isPresent()) {
            return network.get();
        }

        System.out.println("Entrei aq");
        throw new NetworkNotFoundException(String.format(messageService.getMessage("tartarus.blockchain.network.not-found"), networkTag));
    }


    public CompanyNetwork getCompanyNetworkLoggedIn() throws NetworkNotFoundException, CompanyNotLoggedInException, NetworkNotLoggedInException {
        AuthenticationAccount authentication = authService.getCurrentAuthentication();
        CredentialAccount credentialAccount = (CredentialAccount) authentication.getCredentials();
        String networkTag = credentialAccount.getNetworkTag();

        if (networkTag != null) {
            Network network = this.getNetworkByTag(networkTag);
            Company company = companyService.getCompanyLoggedIn();

            CompanyNetworkPK companyNetworkPK = new CompanyNetworkPK(company, network);

            Optional<CompanyNetwork> companyNetwork = companyNetworkDAO.findById(companyNetworkPK);

            if (companyNetwork.isPresent()) {
                return companyNetwork.get();
            }
        }

        throw new NetworkNotLoggedInException(messageService.getMessage("tartarus.blockchain.network.not-logged-in"));
    }


    public AccountCompanyNetwork getAccountCompanyNetworkLoggedIn() throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException {
        CompanyNetwork companyNetwork = this.getCompanyNetworkLoggedIn();
        Account account = accountService.getAccountLoggedIn();

        CompanyNetworkAccountPK pk = new CompanyNetworkAccountPK(companyNetwork, account);

        Optional<AccountCompanyNetwork> companyNetworkAccount = this.accountCompanyNetworkDAO.findById(pk);

        if (companyNetworkAccount.isPresent()) {
            return companyNetworkAccount.get();
        }

        throw new AccountNotRegisteredInCompanyNetworkException(String.format(messageService.getMessage("tartarus.blockchain.network.account-not-registered-in-company-network"), companyNetwork.getNetwork().getNetworkNameFormated()));
    }


    public String getPrivateKey(String password) throws NetworkNotLoggedInException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NetworkNotFoundException, PrivateKeyNotSet, InvalidUsernameOrPasswordException {
        AccountCompanyNetwork accountCompanyNetwork = this.getAccountCompanyNetworkLoggedIn();
        String hashedPassword = accountCompanyNetwork.getAccount().getPassword();

        if (PasswordUtils.verifyPassword(password, hashedPassword)) {

            String hashedPrivateKey = accountCompanyNetwork.getPrivateKey();

            if (hashedPrivateKey != null) {

                try {
                    SecretKeySpec secret = BlockchainUtils.getKeyFromPassword(password);
                    return BlockchainUtils.decryptPrivateKey(hashedPrivateKey, secret);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }

            } else {
                throw new PrivateKeyNotSet(messageService.getMessage("tartarus.blockchain.network.private-key-not-set"));

            }


        }

        throw new InvalidUsernameOrPasswordException(messageService.getMessage("tartarus.validations.password-is-not-valid"));
    }


    public SuccessDTO savePrivateKey(SavePrivateKeyDTO savePrivateKeyDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NetworkNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        AccountCompanyNetwork accountCompanyNetwork = this.getAccountCompanyNetworkLoggedIn();

        String password = savePrivateKeyDTO.getPassword();
        String hashedPassword = accountCompanyNetwork.getAccount().getPassword();

        if (PasswordUtils.verifyPassword(password, hashedPassword)) {

            String privateKeyPlain = savePrivateKeyDTO.getPrivateKey();

            SecretKeySpec secret = BlockchainUtils.getKeyFromPassword(password);
            String hashedPrivateKey = BlockchainUtils.encryptPrivateKey(privateKeyPlain, secret);

            accountCompanyNetwork.setPrivateKey(hashedPrivateKey);

            accountCompanyNetworkDAO.save(accountCompanyNetwork);

            return new SuccessDTO(true, messageService.getMessage("tartarus.blockchain.network.save-private-key"));

        }

        return new SuccessDTO(false, messageService.getMessage("tartarus.validations.password-is-not-valid"));

    }


    public ArrayList<NetworkDTO> getAvailableNetworks() {
        Set<Network> availableNetworks = networkDAO.findByActive(true);


        ArrayList<NetworkDTO> networks = new ArrayList<>();
        for (Network network : availableNetworks) {
            networks.add(new NetworkDTO(network));
        }


        return networks;
    }


    public ArrayList<NetworkDTO> getRegisteredNetworks() {
        Account account = accountService.getAccountLoggedIn();
        Set<AccountCompanyNetwork> accountCompanyNetworks = accountCompanyNetworkDAO.findByAccount(account);


        ArrayList<NetworkDTO> networks = new ArrayList<>();
        for (AccountCompanyNetwork network : accountCompanyNetworks) {
            networks.add(new NetworkDTO(network.getCompanyNetwork().getNetwork()));
        }


        return networks;
    }


}
