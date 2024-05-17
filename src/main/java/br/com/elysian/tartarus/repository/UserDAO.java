package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Account;

public interface UserDAO extends CRUDRepository<Account, Long>{

     Account findByEmailAndPassword(String email, String password);

     boolean existsByEmailAndPassword(String email, String password);
     boolean existsByEmail(String email);

}
