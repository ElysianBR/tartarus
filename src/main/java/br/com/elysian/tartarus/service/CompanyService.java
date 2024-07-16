package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.repository.CompanyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CompanyService {

    private final AccountService accountService;
    private final AuthService authService;
    private final CompanyDAO companyDAO;

    @Autowired
    public CompanyService(AccountService accountService, AuthService authService, CompanyDAO companyDAO) {
        this.accountService = accountService;
        this.authService = authService;
        this.companyDAO = companyDAO;
    }


    public Set<Company> getBusinessFromAccount() {
        Account account = accountService.getAccountLogged();
        return account.getCompanies();
    }

    public Company getCompanyLoggedIn() {
        AuthenticationAccount authentication = authService.getCurrentAuthentication();
        Long companyId = ((CredentialAccount) authentication.getCredentials()).getCompanyId();

        if (companyId != null) {
            if (companyDAO.existsById(companyId)) {
                return companyDAO.findById(companyId).get();
            }
        }

        return null;
    }

}
