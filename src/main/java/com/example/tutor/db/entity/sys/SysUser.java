package com.example.tutor.db.entity.sys;

import com.example.tutor.db.entity.HB.UserPhoto;
import com.example.tutor.db.entity.SubjectExperience;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import com.example.tutor.db.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "sys_users")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "can_comment")
    @Builder.Default
    boolean canComment = true;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_photo_id")
    UserPhoto userPhoto;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    TutorDetails tutorDetails;

    @Column(name = "age")
    Integer age;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "second_name", nullable = false)
    String secondName;

    @Column(name = "email", nullable = false)
    @Email
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "telegram")
    String telegram;

    @Builder.Default
    @Column(name = "phone_number_verified")
    Boolean phoneNumberVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<SysRole> roles = new HashSet<>();

    @Column(name = "last_password")
    String lastPassword;

    @Column(name = "second_last_password")
    String secondLastPassword;

    @Column(name = "third_last_password")
    String thirdLastPassword;

    @Builder.Default
    @Column(name = "failed_login_attempts")
    Integer failedLoginAttempts = 0;

    @Column(name = "last_login")
    Date lastLogin;

    @Builder.Default
    @Column(name = "password_change_next_logon") //Сменить пароль при следующей авторизации
    Boolean passwordChangeNextLogon = true;

    @Column(name = "password_last_change_time") //Время последнего изменения пароля
    @Temporal(TemporalType.TIMESTAMP)
    Date passwordLastChangeTime = new Date();

    @Column(name = "temporary_access_until_time") //Временный доступ до времени
    @Temporal(TemporalType.TIMESTAMP)
    Date temporaryAccessUntilTime;

    @Builder.Default
    @Column(name = "is_banned")
    boolean isBanned = false;

    @Builder.Default
    @Column(name = "email_verified")
    boolean emailVerified = false;

    @Builder.Default
    @Column(name = "deleted")
    boolean deleted = false;

    @Column(name = "edited_time")
    Date editedTime;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;


}
