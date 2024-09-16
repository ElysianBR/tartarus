package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.put.CompanyDTO;
import br.com.elysian.tartarus.exception.CompanyNotFoundException;
import br.com.elysian.tartarus.exception.CompanyNotLoggedInException;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.model.CompanyAccount;
import br.com.elysian.tartarus.model.pk.CompanyAccountPK;
import br.com.elysian.tartarus.repository.CompanyAccountDAO;
import br.com.elysian.tartarus.repository.CompanyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CompanyService {

    private final AccountService accountService;
    private final AuthService authService;
    private final CompanyDAO companyDAO;
    private final MessageService messageService;
    private final CompanyAccountDAO companyAccountDAO;

    @Autowired
    public CompanyService(AccountService accountService, AuthService authService, CompanyDAO companyDAO, MessageService messageService, CompanyAccountDAO companyAccountDAO) {
        this.accountService = accountService;
        this.authService = authService;
        this.companyDAO = companyDAO;
        this.messageService = messageService;
        this.companyAccountDAO = companyAccountDAO;
    }


    public void with(Long companyId) throws CompanyNotFoundException {

        Optional<Company> company = companyDAO.findById(companyId);

        if (company.isPresent()) {
            Account account = accountService.getAccountLoggedIn();

            boolean found = companyAccountDAO.existsById(
                    new CompanyAccountPK(
                            company.get(),
                            account
                    )
            );

            if (found) {
                AuthenticationAccount authenticationAccount = AuthService.getCurrentAuthentication();

                ((CredentialAccount) authenticationAccount.getCredentials()).setCompanyId(companyId);
                return;
            }

        }

        throw new CompanyNotFoundException(String.format(messageService.getMessage("tartarus.company.not-found"), companyId));

    }


    public List<Company> getCompaniesFromAccount() {
        Account account = accountService.getAccountLoggedIn();
        return account.getCompanies()
                .stream()
                .map(CompanyAccount::getCompany)
                .map((c) -> c.isActive() ? c : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Company getCompanyLoggedIn() throws CompanyNotLoggedInException {
        AuthenticationAccount authentication = AuthService.getCurrentAuthentication();
        Long companyId = ((CredentialAccount) authentication.getCredentials()).getCompanyId();

        if (companyId != null) {
            return companyDAO.findById(companyId).orElse(null);
        }

        throw new CompanyNotLoggedInException(messageService.getMessage("tartarus.company.not-logged-in"));
    }

    public Company put(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setCompanyFromdDTO(companyDTO);
        return companyDAO.save(company);
    }


    public Company update(CompanyDTO companyDTO) throws CompanyNotLoggedInException {
        Company company = this.getCompanyLoggedIn();
        company.setCompanyFromdDTO(companyDTO);
        return companyDAO.save(company);
    }


}
