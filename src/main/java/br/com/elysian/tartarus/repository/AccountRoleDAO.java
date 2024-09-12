package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.AccountRole;
import br.com.elysian.tartarus.model.pk.AccountRolePK;

import java.util.List;

public interface AccountRoleDAO extends CRUDRepository<AccountRole, AccountRolePK>{

     List<AccountRole> findByAccountAndIsDefault(Account account, boolean isDefault);

}
