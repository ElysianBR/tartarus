package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.CompanyAccount;
import br.com.elysian.tartarus.model.pk.CompanyAccountPK;

import java.util.Optional;

public interface CompanyAccountDAO extends CRUDRepository<CompanyAccount, CompanyAccountPK>{
}
