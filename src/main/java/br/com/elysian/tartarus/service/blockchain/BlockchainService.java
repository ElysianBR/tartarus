package br.com.elysian.tartarus.service.blockchain;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.model.Blockchain;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.model.CompanyBlockchain;
import br.com.elysian.tartarus.model.CompanyBlockchainPK;
import br.com.elysian.tartarus.repository.BlockchainDAO;
import br.com.elysian.tartarus.repository.CompanyBlockchainDAO;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockchainService {

    private final AuthService authService;
    private final CompanyService companyService;
    private final BlockchainDAO blockchainDAO;
    private final CompanyBlockchainDAO companyBlockchainDAO;

    @Autowired
    public BlockchainService(AuthService authService, CompanyService companyService, BlockchainDAO blockchainDAO, CompanyBlockchainDAO companyBlockchainDAO) {
        this.authService = authService;
        this.companyService = companyService;
        this.blockchainDAO = blockchainDAO;
        this.companyBlockchainDAO = companyBlockchainDAO;
    }





    public Blockchain getBlockchainByTag(String blockchainTag) {
        if (blockchainTag != null) {
            if (blockchainDAO.existsByTag(blockchainTag)) {
                return blockchainDAO.findByTag(blockchainTag);
            }
        }

        return null;
    }

    public CompanyBlockchain getBlockchainLoggedIn() {
        AuthenticationAccount authentication = authService.getCurrentAuthentication();
        CredentialAccount credentialAccount = (CredentialAccount) authentication.getCredentials();
        String blockchainTag = credentialAccount.getBlockchainTag();

        Blockchain blockchain = this.getBlockchainByTag(blockchainTag);
        Company company = companyService.getCompanyLoggedIn();

        System.out.println("blockchain="+blockchain+" company="+company);

        if (company != null && blockchain != null) {
            CompanyBlockchainPK companyBlockchainPKpk = new CompanyBlockchainPK(company, blockchain);
            if (companyBlockchainDAO.existsById(companyBlockchainPKpk)) {
                return companyBlockchainDAO.findById(companyBlockchainPKpk).get();
            }
        }

        return null;
    }

}
