package com.example.tutor.db.entity.HB;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "advertisment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Advertisment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "advertisment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<AdvertismentPhoto> photos;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;

    @Column(name = "description")
    String description;
}
