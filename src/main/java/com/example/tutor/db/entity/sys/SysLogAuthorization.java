package com.example.tutor.db.entity.sys;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "sys_logs_authorization")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SysLogAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    SysUser user;

    @Column(name = "remote_ip", length = 30)
    String remoteIp;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;

    public SysLogAuthorization(SysUser user, String remoteIp) {
        this.user = user;
        this.remoteIp = remoteIp;
    }

    public SysLogAuthorization() {

    }
}
