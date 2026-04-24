package com.edulearn.assessment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "attempts")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;

    @Column(nullable = false)
    private Long quizId;

    @Column(nullable = false)
    private Long studentId;

    private Double score;       // percentage 0-100
    private boolean passed;

    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;

    // key = question orderIndex, value = student's answer string
    @ElementCollection
    @CollectionTable(name = "attempt_answers",
                     joinColumns = @JoinColumn(name = "attempt_id"))
    @MapKeyColumn(name = "question_index")
    @Column(name = "answer")
    private Map<Integer, String> answers;

    @PrePersist
    public void prePersist() { this.startedAt = LocalDateTime.now(); }
}