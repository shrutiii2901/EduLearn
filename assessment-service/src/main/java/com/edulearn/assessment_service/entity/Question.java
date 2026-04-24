package com.edulearn.assessment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "questions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(nullable = false)
    private Long quizId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ElementCollection
    @CollectionTable(name = "question_options",
                     joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options;  // for MCQ; null for true/false

    @Column(nullable = false)
    private String correctAnswer;  // e.g. "A", "True", "B"

    private Double marks = 1.0;
    private Integer orderIndex;

    public enum QuestionType { MCQ, TRUE_FALSE }
}