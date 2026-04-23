package com.edulearn.enrollment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress",
       uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "lessonId"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long lessonId;

    private Integer watchedSeconds = 0;
    private boolean isCompleted = false;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() { this.lastAccessedAt = LocalDateTime.now(); }
}