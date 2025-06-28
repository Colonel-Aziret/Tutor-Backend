package com.example.tutor.db.repository;

import com.example.tutor.db.entity.sys.PasswordResetToken;
import com.example.tutor.db.entity.sys.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :now")
    void deleteByExpiryDateBefore(@Param("now") Date now);

    List<PasswordResetToken> findByUserAndExpiryDateAfter(SysUser user, Date startOfDay);
}
