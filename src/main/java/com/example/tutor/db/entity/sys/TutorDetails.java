package com.example.tutor.db.entity.sys;

import com.example.tutor.db.entity.HB.HBCities;
import com.example.tutor.db.entity.SubjectExperience;
import com.example.tutor.db.entity.TutorReview;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "tutor_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    SysUser user;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<TutorReview> reviews;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<SubjectExperience> subjectExperiences;

    @Column(name = "rate")
    Double rate = 0d;

    @Column(name = "price")
    Double price;

    @Column(name = "experience")
    Integer experience;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    HBCities city;

    @Column(name = "about_me", length = 1000)
    String aboutMe;

    @Column(name = "online")
    Boolean online;

    @Column(name = "offline")
    Boolean offline;

    @Column(name = "at_tutor")
    Boolean atTutor;

    @Column(name = "address")
    String address;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<TutorEducation> educations;
}
