package com.example.tutor.db.entity.sys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private SysUser user;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public PasswordResetToken(String token, SysUser user) {
        this.token = token;
        this.user = user;
        this.expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // Действителен 1 час
    }
}
