package com.example.tutor.db.repository.hb;

import com.example.tutor.db.entity.HB.HBSubjects;
import com.example.tutor.db.repository.AliasRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectsRepository extends JpaRepository<HBSubjects, Long> , JpaSpecificationExecutor<HBSubjects>, AliasRepository<HBSubjects> {
}
