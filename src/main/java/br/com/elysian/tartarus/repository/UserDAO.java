package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.User;

public interface UserDAO extends CRUDRepository<User, Integer>{

     User findByUsernameOrEmailAndPassword(String username, String email, String password);

}
