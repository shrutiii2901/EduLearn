package com.edulearn.enrollment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "courseId"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private Double progressPercent = 0.0;
    private boolean certificateIssued = false;

    @PrePersist
    public void prePersist() { this.enrolledAt = LocalDateTime.now(); }

    public enum Status { ACTIVE, COMPLETED, CANCELLED }
}