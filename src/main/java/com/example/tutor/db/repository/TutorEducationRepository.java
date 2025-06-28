package com.example.tutor.db.repository;

import com.example.tutor.db.entity.sys.TutorDetails;
import com.example.tutor.db.entity.sys.TutorEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorEducationRepository extends JpaRepository<TutorEducation, Long> {
    List<TutorEducation> findByTutor(TutorDetails detailsSaved);

    void deleteAllByTutor(TutorDetails details);
}
