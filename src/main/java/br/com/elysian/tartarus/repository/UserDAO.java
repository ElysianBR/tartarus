package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.User;

public interface UserDAO extends CRUDRepository<User, Integer>{

     User findByUsername(String userName);
     User findByUsernameAndPassword(String userName, String password);

}
