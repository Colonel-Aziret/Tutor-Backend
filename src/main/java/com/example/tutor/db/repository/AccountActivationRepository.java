package com.example.tutor.db.repository;

import com.example.tutor.db.entity.AccountActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountActivationRepository extends JpaRepository<AccountActivation, Long> {

    Optional<AccountActivation> findByActivationCode(UUID activationCode);

    List<AccountActivation> findAllByUserId(Long userId);
}
