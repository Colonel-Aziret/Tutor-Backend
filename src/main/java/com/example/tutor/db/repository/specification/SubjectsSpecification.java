package com.example.tutor.db.repository.specification;

import com.example.tutor.db.entity.HB.HBSubjects;
import com.example.tutor.model.subjects.SubjectsFilterDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SubjectsSpecification implements Specification<HBSubjects> {

    private final SubjectsFilterDto filter;

    @Override
    public jakarta.persistence.criteria.Predicate toPredicate(Root<HBSubjects> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (Boolean.FALSE.equals(filter.getDeleted())) {
            predicates.add(cb.isFalse(root.get("deleted")));
        } else if (Boolean.TRUE.equals(filter.getDeleted())) {
            predicates.add(cb.isTrue(root.get("deleted")));
        }
        if (filter.getAlias() != null && !filter.getAlias().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("alias")), "%" + filter.getAlias().toLowerCase() + "%"));
        }

        if (filter.getName() != null && !filter.getName().isBlank()) {
            String pattern = "%" + filter.getName().toLowerCase() + "%";
            Predicate nameRuMatch = cb.like(cb.lower(root.get("nameRu")), pattern);
            Predicate nameKyMatch = cb.like(cb.lower(root.get("nameKy")), pattern);
            predicates.add(cb.or(nameRuMatch, nameKyMatch));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}



