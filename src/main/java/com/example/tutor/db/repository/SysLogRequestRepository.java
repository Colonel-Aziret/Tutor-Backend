package com.example.tutor.db.repository;


import com.example.tutor.db.entity.sys.SysLogRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogRequestRepository extends JpaRepository<SysLogRequest, Long> {
}
