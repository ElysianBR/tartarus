package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Account;

public interface AccountDAO extends CRUDRepository<Account, Long>{

     boolean existsByEmail(String email);

     Account findByEmail(String email);

}
