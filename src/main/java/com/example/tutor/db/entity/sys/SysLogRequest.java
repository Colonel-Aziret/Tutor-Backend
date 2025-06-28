package com.example.tutor.db.entity.sys;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "sys_logs_request")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SysLogRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "class_name", nullable = false)
    String className;

    @Column(name = "method_name", nullable = false)
    String methodName;

    @Column(name = "details", length = 5000)
    String details;

    @Column(name = "status", nullable = false)
    String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    SysUser user;

    @Column(name = "remote_ip", length = 30)
    String remoteIp;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;

    public SysLogRequest(String className, String methodName, String details, String status, SysUser user, String remoteIp) {
        this.className = className;
        this.methodName = methodName;
        this.details = details;
        this.status = status;
        this.user = user;
        this.remoteIp = remoteIp;
    }

    public SysLogRequest() {
    }
}
