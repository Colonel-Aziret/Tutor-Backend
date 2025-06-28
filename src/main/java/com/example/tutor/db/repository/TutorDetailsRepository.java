package com.example.tutor.db.repository;

import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.entity.sys.TutorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorDetailsRepository extends JpaRepository<TutorDetails, Long> {
    Optional<TutorDetails> findByUser(TutorDetails user);
    Optional<TutorDetails> findByUserId(Long user_id);

    @Query("SELECT t FROM TutorDetails t LEFT JOIN FETCH t.reviews WHERE t.id = :id")
    Optional<TutorDetails> findByIdWithReviews(@Param("id") Long id);}
