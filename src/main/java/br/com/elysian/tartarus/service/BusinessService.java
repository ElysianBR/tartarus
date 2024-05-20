package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.dto.BusinessDTO;
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


    public ArrayList<BusinessDTO> getBusinessFromAccountLogged(boolean activeOnly) {
        Account account = accountService.getAccountLogged();
        Set<Business> businessSet = account.getBusiness();

        ArrayList<BusinessDTO> businessDTOs = new ArrayList<>();

        businessSet.forEach(business -> {
            if (!activeOnly || business.isActive()) {
                businessDTOs.add(new BusinessDTO(business));
            }
        });


        return businessDTOs;
    }

}
