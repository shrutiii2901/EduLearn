package com.edulearn.course_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private String contentUrl;

    private Integer durationMinutes;

    private Integer orderIndex;

    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean isPreview = false;

    public enum ContentType { VIDEO, ARTICLE, PDF }
}