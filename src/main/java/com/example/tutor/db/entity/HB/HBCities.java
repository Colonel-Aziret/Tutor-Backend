package com.example.tutor.db.entity.HB;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "hb_cities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HBCities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "alias", unique = true)
    String alias;

    @Column(name = "name_ru")
    String nameRu;

    @Column(name = "name_ky")
    String nameKy;

    @Builder.Default
    @Column(name = "deleted")
    boolean deleted = false;

    @Column(name = "edited_time")
    Date editedTime;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;
}
