package com.example.tutor.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "error_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    @Id
    @Column(name = "id", nullable = false)
    String id;

    @Column(name = "field", nullable = false)
    String field;

    @Column(name = "message_ru", nullable = false)
    String messageRu;

    @Column(name = "message_kg", nullable = false)
    String messageKg;

    @Column(name = "message_eng", nullable = false)
    String messageEng;
}
