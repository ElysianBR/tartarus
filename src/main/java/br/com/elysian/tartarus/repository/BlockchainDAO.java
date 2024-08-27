package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Blockchain;

import java.util.Optional;

public interface BlockchainDAO extends CRUDRepository<Blockchain, Long>{

    boolean existsByTag(String tag);
    Optional<Blockchain> findByTag(String tag);

}
