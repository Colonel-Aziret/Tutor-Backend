package com.example.tutor.db.repository;

import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.entity.sys.TutorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TutorReviewRepository extends JpaRepository<TutorReview, Long>, JpaSpecificationExecutor<TutorReview> {
    List<TutorReview> findByTutor(TutorDetails tutor);

    List<TutorReview> findByAuthor(SysUser user);

    @Modifying
    @Query("DELETE FROM TutorReview tr WHERE tr.author.id = :authorId")
    void deleteAllByAuthorId(@Param("authorId") Long authorId);
}
