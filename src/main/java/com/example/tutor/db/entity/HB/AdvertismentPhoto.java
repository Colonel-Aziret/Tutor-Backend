package com.example.tutor.db.entity.HB;

import com.example.tutor.db.entity.sys.SysUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "advertisment_photo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdvertismentPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "advertisment_id", nullable = false)
    Advertisment advertisment;

    @Column(name = "file_path", nullable = false)
    String filePath;

    @Column(name = "file_path_thumb")
    String filePathThumb;

    @Column(name = "file_path_large")
    String filePathLarge;

    @Column(name = "content_type")
    String contentType;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;
}
