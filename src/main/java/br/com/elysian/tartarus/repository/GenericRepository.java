package br.com.elysian.tartarus.repository;

import br.com.elysian.tartarus.dto.search.criteria.FieldCriteriaDTO;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenericRepository<T> implements Specification<T> {

    private final List<String> columns;
    private final Collection<FieldCriteriaDTO> criterias;


    public GenericRepository(Collection<FieldCriteriaDTO> criterias, List<String> columns) {
        this.criterias = criterias;
        this.columns = columns;
    }


    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (FieldCriteriaDTO criteria : criterias) {
            Expression<String> expression = buildExpression(criteria.getFieldName(), root);

            Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(expression), criteria.getValue().toString().toLowerCase());

            predicates.add(predicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Expression<String> buildExpression(String column, Path<T> path) {
        if (!column.contains("."))
            return path.get(column);

        String[] parts = column.split("\\.");
        int i = 0;
        for (; i < parts.length - 1; i++) {
            path = path.get(parts[i]);
        }

        return path.get(parts[i]);
    }
}