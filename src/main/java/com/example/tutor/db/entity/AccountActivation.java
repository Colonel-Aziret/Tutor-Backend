package com.example.tutor.db.entity;

import com.example.tutor.db.entity.sys.SysUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "account_activation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    SysUser user;

    @Column(name = "activation_code", nullable = false, unique = true)
    UUID activationCode;

    @Column(name = "expires_at", nullable = false)
    Date expiresAt;

    @Column(name = "activated", nullable = false)
    boolean activated;

    @Column(name = "edited_time")
    Date editedTime;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;

    public AccountActivation(SysUser user) {
        this.user = user;
        this.activationCode = UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 8);
        this.expiresAt = calendar.getTime();
    }

}
