package com.example.tutor.db.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import com.example.tutor.db.entity.ActionLog;
import com.example.tutor.model.actionLog.filter.ActionLogFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ActionLogSpecification implements Specification<ActionLog> {

    private final ActionLogFilter filter;


    @Override
    public Predicate toPredicate(Root<ActionLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getUserIds() != null && !filter.getUserIds().isEmpty()) {
            predicates.add(root.get("user").get("id").in(filter.getUserIds()));
        }

        if (filter.getObjectType() != null) {
            predicates.add(cb.equal(root.get("objectType"), filter.getObjectType()));
        }

        if (filter.getMethodType() != null) {
            predicates.add(cb.equal(root.get("httpMethodType"), filter.getMethodType()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
