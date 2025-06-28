package com.example.tutor.db.entity.sys;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "sys_roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "alias", nullable = false, unique = true)
    String alias;

    @Column(name = "name_ru", nullable = false)
    String nameRu;

    @Column(name = "name_ky", nullable = false)
    String nameKy;

    @Builder.Default
    @Column(name = "password_length", nullable = false)
    Integer passwordLength = 8;

    @Builder.Default
    @Column(name = "deleted")
    boolean deleted = false;

    @Column(name = "edited_time")
    Date editedTime;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;

}
