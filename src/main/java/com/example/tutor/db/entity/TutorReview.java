package com.example.tutor.db.entity;

import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.entity.sys.TutorDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "tutor_reviews")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tutor_id", nullable = false)
    TutorDetails tutor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    SysUser author;

    @Column(name = "comment", nullable = false, length = 1000)
    String comment;

    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = false)
    Integer rating;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    Date createdTime;
}
