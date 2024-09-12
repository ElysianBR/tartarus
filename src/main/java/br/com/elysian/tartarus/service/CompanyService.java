package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.PutCompanyDTO;
import br.com.elysian.tartarus.exception.CompanyNotFoundException;
import br.com.elysian.tartarus.exception.CompanyNotLoggedInException;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.repository.CompanyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CompanyService {

    private final AccountService accountService;
    private final AuthService authService;
    private final CompanyDAO companyDAO;
    private final MessageService messageService;

    @Autowired
    public CompanyService(AccountService accountService, AuthService authService, CompanyDAO companyDAO, MessageService messageService) {
        this.accountService = accountService;
        this.authService = authService;
        this.companyDAO = companyDAO;
        this.messageService = messageService;
    }


    public void with(Long companyId) throws CompanyNotFoundException {
        AuthenticationAccount authenticationAccount = AuthService.getCurrentAuthentication();

        if (companyDAO.existsById(companyId)) {
            ((CredentialAccount) authenticationAccount.getCredentials()).setCompanyId(companyId);
        } else {
            throw new CompanyNotFoundException(String.format(messageService.getMessage("tartarus.company.not-found"), companyId));
        }
    }


    public Set<Company> getCompaniesFromAccount() {
        Account account = accountService.getAccountLoggedIn();
        return account.getCompanies();
    }

    public Company getCompanyLoggedIn() throws CompanyNotLoggedInException {
        AuthenticationAccount authentication = AuthService.getCurrentAuthentication();
        Long companyId = ((CredentialAccount) authentication.getCredentials()).getCompanyId();

        if (companyId != null) {
            return companyDAO.findById(companyId).orElse(null);
        }

        throw new CompanyNotLoggedInException(messageService.getMessage("tartarus.company.not-logged-in"));
    }

    public Company put(PutCompanyDTO companyDTO) {
        Company company = new Company();
        company.setCompanyFromdDTO(companyDTO);
        return companyDAO.save(company);
    }


    public Company update(PutCompanyDTO companyDTO) throws CompanyNotLoggedInException {
        Company company = this.getCompanyLoggedIn();
        company.setCompanyFromdDTO(companyDTO);
        return companyDAO.save(company);
    }


}
