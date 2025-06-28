package com.example.tutor.db.repository;

import com.example.tutor.db.entity.SubjectExperience;
import com.example.tutor.db.entity.sys.TutorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectExperienceRepository extends JpaRepository<SubjectExperience, Long> {
    List<SubjectExperience> findByTutor(TutorDetails detailsSaved);

    void deleteAllByTutor(TutorDetails details);
}
