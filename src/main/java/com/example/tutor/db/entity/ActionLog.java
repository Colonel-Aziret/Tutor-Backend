package com.example.tutor.db.entity;

import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.enums.HttpMethodType;
import com.example.tutor.db.enums.ProcessObjectType;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "action_logs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    SysUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type", nullable = false)
    ProcessObjectType objectType;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method_type", nullable = false)
    HttpMethodType httpMethodType;

    @Column(name = "details", length = 5000)
    String details;

    @Column(name = "remote_ip", length = 30)
    String remoteIp;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;

}
