package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Account;

import java.util.Optional;

public interface AccountDAO extends CRUDRepository<Account, Long>{

     boolean existsByEmail(String email);

     Optional<Account> findByEmail(String email);

}
