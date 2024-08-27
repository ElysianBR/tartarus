package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.model.Blockchain;
import br.com.elysian.tartarus.model.Network;

import java.util.Optional;
import java.util.Set;

public interface NetworkDAO extends CRUDRepository<Network, Long>{

    boolean existsByTag(String tag);
    Optional<Network> findByTag(String tag);

    Set<Network> findByActive(boolean active);

}
