package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.User;

public interface UserDAO extends CRUDRepository<User, Integer>{

     User findByUserName(String userName);
     User findByUserNameAndPassword(String userName, String password);

}
