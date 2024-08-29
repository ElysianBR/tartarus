package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.CompanyNetworkAccount;
import br.com.elysian.tartarus.model.pk.CompanyNetworkAccountPK;
import br.com.elysian.tartarus.model.pk.CompanyNetworkPK;

import java.util.Set;

public interface CompanyNetworkDAO extends CRUDRepository<CompanyNetwork, CompanyNetworkPK>{
}
