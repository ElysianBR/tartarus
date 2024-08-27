package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.CompanyNetworkAccount;
import br.com.elysian.tartarus.model.pk.CompanyNetworkAccountPK;

import java.util.Set;

public interface CompanyNetworkAccountDAO extends CRUDRepository<CompanyNetworkAccount, CompanyNetworkAccountPK>{

    Set<CompanyNetworkAccount> findByAccount(Account account);

}
