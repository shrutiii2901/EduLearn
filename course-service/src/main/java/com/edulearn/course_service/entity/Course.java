package com.edulearn.course_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;

    @Enumerated(EnumType.STRING)
    private Level level;

    private BigDecimal price;

    @Column(nullable = false)
    private Long instructorId;

    private String thumbnailUrl;

    private Integer totalDurationMinutes;

    private boolean isPublished = false;

    private String language;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


    public enum Level { BEGINNER, INTERMEDIATE, ADVANCED }
    public enum ApprovalStatus { PENDING, APPROVED, REJECTED }
}