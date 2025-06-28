package com.example.tutor.db.entity;

import com.example.tutor.db.entity.HB.HBSubjects;
import com.example.tutor.db.entity.sys.TutorDetails;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "subject_experience")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    HBSubjects subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tutor_id", nullable = false)
    TutorDetails tutor;

    @Column(name = "experience_description", length = 1000)
    String experienceDescription;
}
