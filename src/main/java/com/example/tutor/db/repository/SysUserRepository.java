package com.example.tutor.db.repository;


import com.example.tutor.db.entity.sys.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {
    @Query("SELECT u FROM SysUser u where  u.email = :login and u.deleted is false")
    Optional<SysUser> findByLoginAndActive(String login);

    Optional<SysUser> findByIdAndDeletedFalse(Long id);

    Optional<SysUser> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<SysUser> findByRolesAlias(String role);

    Optional<SysUser> findByPhoneNumber(String phoneNumber);

    @EntityGraph(attributePaths = {
            "tutorDetails",
            "tutorDetails.city",
            "tutorDetails.educations",
            "tutorDetails.subjectExperiences",
            "tutorDetails.reviews"
    })
    Optional<SysUser> findWithTutorDetailsById(Long id);

    @Query("SELECT u FROM SysUser u LEFT JOIN FETCH u.tutorDetails t LEFT JOIN FETCH t.reviews WHERE u.id = :id")
    Optional<SysUser> findByIdWithTutorDetailsAndReviews(@Param("id") Long id);


}
