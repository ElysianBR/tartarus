package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Blockchain;

public interface BlockchainDAO extends CRUDRepository<Blockchain, Long>{

    boolean existsByTag(String tag);
    Blockchain findByTag(String tag);

}
