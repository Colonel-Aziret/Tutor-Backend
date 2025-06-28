package com.example.tutor.db.repository.specification;

import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.model.tutorDetails.TutorReviewFilterDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TutorReviewSpecification implements Specification<TutorReview> {

    private final TutorReviewFilterDto filter;

    @Override
    public Predicate toPredicate(Root<TutorReview> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getTutorId() != null) {
            var tutorJoin = root.join("tutor"); // join TutorDetails
            var userJoin = tutorJoin.join("user"); // join SysUser
            predicates.add(cb.equal(userJoin.get("id"), filter.getTutorId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
