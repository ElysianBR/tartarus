package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.dto.v1.BusinessDTO;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class BusinessService {

    private final AccountService accountService;

    @Autowired
    public BusinessService(AccountService accountService) {
        this.accountService = accountService;
    }


    public Set<Business> getBusinessFromAccount() {
        Account account = accountService.getAccountLogged();
        return account.getBusiness();
    }

}
