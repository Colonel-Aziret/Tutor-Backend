package com.example.tutor.db.repository.specification;

import com.example.tutor.db.entity.SubjectExperience;
import com.example.tutor.db.entity.sys.TutorDetails;
import jakarta.persistence.criteria.*;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.model.user.filter.UserFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SysUserSpecification implements Specification<SysUser> {

    private final UserFilterDto filter;

    @Override
    public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter != null) {

            if (filter.getIsBanned() != null) {
                predicates.add(cb.equal(root.get("isBanned"), filter.getIsBanned()));
            }

            Join<SysUser, TutorDetails> tutorJoin = root.join("tutorDetails", JoinType.LEFT);

            if (filter.getMinRate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(tutorJoin.get("rate"), filter.getMinRate()));
            }

            if (filter.getMaxRate() != null) {
                predicates.add(cb.lessThanOrEqualTo(tutorJoin.get("rate"), filter.getMaxRate()));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(tutorJoin.get("price"), filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(tutorJoin.get("price"), filter.getMaxPrice()));
            }

            if (filter.getCityId() != null) {
                predicates.add(cb.equal(tutorJoin.get("city").get("id"), filter.getCityId()));
            }

            if (filter.getOnline() != null) {
                predicates.add(cb.equal(tutorJoin.get("online"), filter.getOnline()));
            }

            if (filter.getOffline() != null) {
                predicates.add(cb.equal(tutorJoin.get("offline"), filter.getOffline()));
            }

            if (filter.getAtTutor() != null) {
                predicates.add(cb.equal(tutorJoin.get("atTutor"), filter.getAtTutor()));
            }

            if (filter.getFullName() != null && !filter.getFullName().isEmpty()) {
                String[] nameParts = filter.getFullName().toLowerCase().split(" ");
                List<Predicate> namePredicates = new ArrayList<>();
                for (String part : nameParts) {
                    namePredicates.add(cb.or(
                            cb.like(cb.lower(root.get("secondName")), part + "%"),
                            cb.like(cb.lower(root.get("name")), part + "%"),
                            cb.like(cb.lower(root.get("patronymic")), part + "%")
                    ));
                }
                predicates.add(cb.and(namePredicates.toArray(new Predicate[0])));
            }

            if (filter.getDeleted() != null) {
                predicates.add(cb.equal(root.get("deleted"), filter.getDeleted()));
            }

            if (filter.getRolesIds() != null && !filter.getRolesIds().isEmpty()) {
                Join<Object, Object> rolesJoin = root.join("roles");
                predicates.add(rolesJoin.get("id").in(filter.getRolesIds()));
            }

            if (filter.getTelegram() != null && !filter.getTelegram().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("telegram")), "%" + filter.getTelegram().toLowerCase() + "%"));
            }

            if (filter.getExperience() != null) {
                Join<TutorDetails, SubjectExperience> expJoin = tutorJoin.join("subjectExperiences", JoinType.LEFT);
                // Предположим, что есть поле "years" или аналогичное
                predicates.add(cb.greaterThanOrEqualTo(expJoin.get("years"), filter.getExperience()));
            }

            if (filter.getIsTutor() != null) {
                Join<Object, Object> rolesJoin = root.join("roles");
                if (filter.getIsTutor()) {
                    predicates.add(cb.equal(cb.upper(rolesJoin.get("alias")), "TUTOR"));
                } else {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<SysUser> subRoot = subquery.from(SysUser.class);
                    Join<Object, Object> subRoles = subRoot.join("roles");
                    subquery.select(subRoot.get("id"))
                            .where(cb.equal(cb.upper(subRoles.get("alias")), "TUTOR"));

                    predicates.add(cb.not(root.get("id").in(subquery)));
                }
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
