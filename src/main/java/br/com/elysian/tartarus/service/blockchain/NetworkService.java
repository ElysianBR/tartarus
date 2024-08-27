package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.blockchain.NetworkDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.model.pk.CompanyNetworkAccountPK;
import br.com.elysian.tartarus.model.pk.CompanyNetworkPK;
import br.com.elysian.tartarus.repository.*;
import br.com.elysian.tartarus.service.AccountService;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.CompanyService;
import br.com.elysian.tartarus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class NetworkService {

    private final AuthService authService;
    private final AccountService accountService;
    private final CompanyService companyService;
    private final NetworkDAO networkDAO;
    private final CompanyNetworkContractDAO companyNetworkContractDAO;
    private final MessageService messageService;
    final CompanyNetworkAccountDAO companyNetworkAccountDAO;


    @Autowired
    public NetworkService(AuthService authService, AccountService accountService, CompanyService companyService, NetworkDAO networkDAO,
                          CompanyNetworkContractDAO companyNetworkContractDAO, MessageService messageService, CompanyNetworkAccountDAO companyNetworkAccountDAO) {
        this.authService = authService;
        this.accountService = accountService;
        this.companyService = companyService;
        this.networkDAO = networkDAO;
        this.companyNetworkContractDAO = companyNetworkContractDAO;
        this.messageService = messageService;
        this.companyNetworkAccountDAO = companyNetworkAccountDAO;
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

            Optional<CompanyNetwork> companyNetwork = companyNetworkContractDAO.findById(companyNetworkPK);

            if (companyNetwork.isPresent()) {
                return companyNetwork.get();
            }
        }

        throw new NetworkNotLoggedInException(messageService.getMessage("tartarus.blockchain.network.not-logged-in"));
    }


    public CompanyNetworkAccount getCompanyNetworkAccountLoggedIn() throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, AccountNotRegisteredInCompanyNetwork {
        CompanyNetwork companyNetwork = this.getCompanyNetworkLoggedIn();
        Account account = accountService.getAccountLoggedIn();

        CompanyNetworkAccountPK pk = new CompanyNetworkAccountPK(companyNetwork, account);

        Optional<CompanyNetworkAccount> companyNetworkAccount = this.companyNetworkAccountDAO.findById(pk);

        if (companyNetworkAccount.isPresent()) {
            return companyNetworkAccount.get();
        }

        throw new AccountNotRegisteredInCompanyNetwork(String.format(messageService.getMessage("tartarus.blockchain.network.account-not-registered-in-company-network"), companyNetwork.getNetwork().getNetworkNameFormated()));
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
        Set<CompanyNetworkAccount> companyNetworkAccounts = companyNetworkAccountDAO.findByAccount(account);


        ArrayList<NetworkDTO> networks = new ArrayList<>();
        for (CompanyNetworkAccount network : companyNetworkAccounts) {
            networks.add(new NetworkDTO(network.getCompanyNetwork().getNetwork()));
        }


        return networks;
    }


}
