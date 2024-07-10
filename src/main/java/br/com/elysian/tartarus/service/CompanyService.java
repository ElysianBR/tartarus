package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.AuthenticationAccount;
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
    private final CompanyDAO companyDAO;

    @Autowired
    public CompanyService(AccountService accountService, CompanyDAO companyDAO) {
        this.accountService = accountService;
        this.companyDAO = companyDAO;
    }


    public Set<Company> getBusinessFromAccount() {
        Account account = accountService.getAccountLogged();
        return account.getCompanies();
    }

    public Company getCompanyLoggedIn() {
        AuthenticationAccount authentication = (AuthenticationAccount) SecurityContextHolder.getContext().getAuthentication();
        Long idCompany = authentication.getIdCompany();

        if (idCompany != null) {
            if (companyDAO.existsById(idCompany)) {
                return companyDAO.findById(idCompany).get();
            }
        }

        return null;
    }

}
