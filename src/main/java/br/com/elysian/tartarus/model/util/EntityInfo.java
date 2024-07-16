package br.com.elysian.tartarus.model.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.MappingMetamodel;
import org.hibernate.metamodel.mapping.EntityIdentifierMapping;
import org.hibernate.metamodel.mapping.TableDetails;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityInfo {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public EntityInfo(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public String getEntityInfo(Class<?> clazz) {
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        if (sessionFactory == null) {
            throw new NullPointerException("Factory is not a hibernate factory");
        }

        MappingMetamodel metamodel = (sessionFactory).getMappingMetamodel();
        EntityPersister entityPersister = metamodel.getEntityDescriptor(clazz);

        if (entityPersister == null) {
            return "No metadata found for " + clazz.getName();
        }

        // Obtendo as chaves primárias
        List<? extends TableDetails.KeyColumn> listKeys = entityPersister.getIdentifierTableDetails().getKeyDetails().getKeyColumns();

        List<String> primaryKeysArray = new ArrayList<>();
        for (TableDetails.KeyColumn key : listKeys) {
            primaryKeysArray.add(key.getColumnName());
        }
        String primaryKeys = String.join(", ", primaryKeysArray);

        // Montando o resultado
        StringBuilder result = new StringBuilder();
        result.append("Entity Name: ").append(clazz.getName()).append("\n");
        result.append("Primary Keys: ").append(primaryKeys).append("\n");

        // Adicione outras informações que você precisa aqui

        return result.toString();
    }
}
