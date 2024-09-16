package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.SuccessDTO;
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
import br.com.elysian.tartarus.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class NetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);


    private final AccountService accountService;
    private final AuthService authService;
    private final CompanyService companyService;
    private final MessageService messageService;


    private final AccountCompanyNetworkDAO accountCompanyNetworkDAO;
    private final CompanyNetworkDAO companyNetworkDAO;
    private final NetworkDAO networkDAO;


    @Autowired
    public NetworkService(AccountService accountService, AuthService authService, CompanyService companyService, MessageService messageService,
                          AccountCompanyNetworkDAO accountCompanyNetworkDAO, CompanyNetworkDAO companyNetworkDAO, NetworkDAO networkDAO) {

        this.accountService = accountService;
        this.authService = authService;
        this.companyService = companyService;
        this.messageService = messageService;

        this.accountCompanyNetworkDAO = accountCompanyNetworkDAO;
        this.companyNetworkDAO = companyNetworkDAO;
        this.networkDAO = networkDAO;

    }


    public void patch(String networkTag) throws NetworkNotFoundException, CompanyNotLoggedInException {

        Optional<Network> network = networkDAO.findByTag(networkTag);

        if (network.isPresent()) {

            Company company = companyService.getCompanyLoggedIn();

            boolean found = companyNetworkDAO.existsById(
                    new CompanyNetworkPK(
                            company,
                            network.get()
                    )
            );


            if (found) {
                AuthenticationAccount authenticationAccount = AuthService.getCurrentAuthentication();

                ((CredentialAccount) authenticationAccount.getCredentials()).setNetworkTag(networkTag);
                return;
            }
        }

        throw new NetworkNotFoundException(String.format(messageService.getMessage("tartarus.blockchain.network.not-found"), networkTag));

    }


    public Network getNetworkByTag(String networkTag) throws NetworkNotFoundException {
        Optional<Network> network = networkDAO.findByTag(networkTag);

        if (network.isPresent()) {
            return network.get();
        }

        throw new NetworkNotFoundException(String.format(messageService.getMessage("tartarus.blockchain.network.not-found"), networkTag));
    }


    public CompanyNetwork getCompanyNetworkLoggedIn() throws NetworkNotFoundException, CompanyNotLoggedInException, NetworkNotLoggedInException {
        AuthenticationAccount authentication = AuthService.getCurrentAuthentication();
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


    public CompanyNetworkAccount getAccountCompanyNetworkLoggedIn() throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetworkException {
        CompanyNetwork companyNetwork = this.getCompanyNetworkLoggedIn();
        Account account = accountService.getAccountLoggedIn();

        CompanyNetworkAccountPK pk = new CompanyNetworkAccountPK(companyNetwork, account);

        Optional<CompanyNetworkAccount> companyNetworkAccount = this.accountCompanyNetworkDAO.findById(pk);

        if (companyNetworkAccount.isPresent()) {
            return companyNetworkAccount.get();
        }

        throw new AccountNotRegisteredInCompanyNetworkException(String.format(messageService.getMessage("tartarus.blockchain.network.account-not-registered-in-company-network"), companyNetwork.getNetwork().getNetworkNameFormated()));
    }


    public String getPrivateKey(String password) throws NetworkNotLoggedInException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NetworkNotFoundException, PrivateKeyNotSet, InvalidUsernameOrPasswordException {
        CompanyNetworkAccount companyNetworkAccount = this.getAccountCompanyNetworkLoggedIn();
        String hashedPassword = companyNetworkAccount.getAccount().getPassword();

        if (PasswordUtils.verifyPassword(password, hashedPassword)) {

            String hashedPrivateKey = companyNetworkAccount.getPrivateKey();

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
        CompanyNetworkAccount companyNetworkAccount = this.getAccountCompanyNetworkLoggedIn();

        String password = savePrivateKeyDTO.getPassword();
        String hashedPassword = companyNetworkAccount.getAccount().getPassword();

        if (PasswordUtils.verifyPassword(password, hashedPassword)) {

            String privateKeyPlain = savePrivateKeyDTO.getPrivateKey();

            SecretKeySpec secret = BlockchainUtils.getKeyFromPassword(password);
            String hashedPrivateKey = BlockchainUtils.encryptPrivateKey(privateKeyPlain, secret);

            companyNetworkAccount.setPrivateKey(hashedPrivateKey);

            accountCompanyNetworkDAO.save(companyNetworkAccount);

            return new SuccessDTO(true, messageService.getMessage("tartarus.blockchain.network.save-private-key"));

        }

        return new SuccessDTO(false, messageService.getMessage("tartarus.validations.password-is-not-valid"));

    }


    public Set<Network> getAvailableNetworks() {
        return networkDAO.findByActive(true);
    }


    public ArrayList<Network> getRegisteredNetworks() {
        Account account = accountService.getAccountLoggedIn();
        Set<CompanyNetworkAccount> companyNetworkAccounts = accountCompanyNetworkDAO.findByAccount(account);


        ArrayList<Network> networks = new ArrayList<>();
        for (CompanyNetworkAccount network : companyNetworkAccounts) {
            networks.add(network.getCompanyNetwork().getNetwork());
        }


        return networks;
    }

}
