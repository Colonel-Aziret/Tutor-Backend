package com.example.tutor.db.entity;

import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.entity.sys.TutorDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    TutorReview parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TutorReview> replies = new ArrayList<>();

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
