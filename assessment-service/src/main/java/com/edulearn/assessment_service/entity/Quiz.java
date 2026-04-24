package com.edulearn.assessment_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quizzes")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Column(nullable = false)
    private Long courseId;

    private Long lessonId; // optional: quiz linked to a specific lesson

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer timeLimitMinutes;
    private Double passingScore = 60.0;  // percentage
    private Integer maxAttempts = 3;
    private boolean isPublished = false;
}