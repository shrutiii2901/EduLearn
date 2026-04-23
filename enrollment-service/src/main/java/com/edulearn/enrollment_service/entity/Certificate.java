package com.edulearn.enrollment_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certificates")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    private String studentName;
    private String courseName;
    private String instructorName;

    private LocalDateTime issuedAt;

    @Column(unique = true)
    private String verificationCode;

    private String certificateUrl;

    @PrePersist
    public void prePersist() {
        this.issuedAt = LocalDateTime.now();
        this.verificationCode = UUID.randomUUID().toString();
    }
}