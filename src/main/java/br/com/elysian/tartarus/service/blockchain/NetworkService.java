package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.exception.BlockchainNotFoundException;
import br.com.elysian.tartarus.exception.CompanyNotLoggedInException;
import br.com.elysian.tartarus.exception.ContractNotFoundException;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.repository.BlockchainDAO;
import br.com.elysian.tartarus.repository.CompanyBlockchainDAO;
import br.com.elysian.tartarus.repository.ContractDAO;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.CompanyService;
import br.com.elysian.tartarus.service.MessageService;
import br.com.elysian.tartarus.service.blockchain.ethereum.EtherClient;
import br.com.elysian.tartarus.exception.BlockchainNotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockchainService {

    private final AuthService authService;
    private final CompanyService companyService;
    private final BlockchainDAO blockchainDAO;
    private final CompanyBlockchainDAO companyBlockchainDAO;
    private final MessageService messageService;
    private final ContractDAO contractDAO;

    @Autowired
    public BlockchainService(AuthService authService, CompanyService companyService, BlockchainDAO blockchainDAO,
                             CompanyBlockchainDAO companyBlockchainDAO, MessageService messageService,
                             ContractDAO contractDAO) {
        this.authService = authService;
        this.companyService = companyService;
        this.blockchainDAO = blockchainDAO;
        this.companyBlockchainDAO = companyBlockchainDAO;
        this.messageService = messageService;
        this.contractDAO = contractDAO;
    }


    public void swith(String blockchainTag) throws BlockchainNotFoundException {
        AuthenticationAccount authenticationAccount = authService.getCurrentAuthentication();

        if (blockchainDAO.existsByTag(blockchainTag)) {
            ((CredentialAccount) authenticationAccount.getCredentials()).setBlockchainTag(blockchainTag);
        } else {
            throw new BlockchainNotFoundException(String.format(messageService.getMessage("tartarus.blochain.not.found"), blockchainTag));
        }
    }


    public Blockchain getBlockchainByTag(String blockchainTag) throws BlockchainNotFoundException {
        if (blockchainDAO.existsByTag(blockchainTag)) {
            return blockchainDAO.findByTag(blockchainTag);
        }

        throw new BlockchainNotFoundException(String.format(messageService.getMessage("tartarus.blochain.not.found"), blockchainTag));
    }


    public CompanyBlockchain getBlockchainLoggedIn() throws BlockchainNotFoundException, CompanyNotLoggedInException, BlockchainNotLoggedInException {
        AuthenticationAccount authentication = authService.getCurrentAuthentication();
        CredentialAccount credentialAccount = (CredentialAccount) authentication.getCredentials();
        String blockchainTag = credentialAccount.getBlockchainTag();

        Blockchain blockchain = this.getBlockchainByTag(blockchainTag);
        Company company = companyService.getCompanyLoggedIn();

        CompanyBlockchainPK companyBlockchainPKpk = new CompanyBlockchainPK(company, blockchain);
        if (companyBlockchainDAO.existsById(companyBlockchainPKpk)) {
            return companyBlockchainDAO.findById(companyBlockchainPKpk).get();
        }

        throw new BlockchainNotLoggedInException(messageService.getMessage("tartarus.blockchain.not.logged.in"));
    }


    public Contract getContract(String contractName, String version) throws ContractNotFoundException {
        if (this.contractDAO.existsByNameAndVersion(contractName, version)) {
            return this.contractDAO.findByNameAndVersion(contractName, version);
        }

        throw new ContractNotFoundException(String.format(messageService.getMessage("tartarus.blockchain.contract.not-found"), contractName, version));
    }


}
