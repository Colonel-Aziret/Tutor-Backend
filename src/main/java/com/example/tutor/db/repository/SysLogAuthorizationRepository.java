package com.example.tutor.db.repository;


import com.example.tutor.db.entity.sys.SysLogAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogAuthorizationRepository extends JpaRepository<SysLogAuthorization, Long> {
}
