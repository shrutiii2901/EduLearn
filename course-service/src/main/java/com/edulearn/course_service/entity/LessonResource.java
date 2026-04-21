package com.edulearn.course_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lesson_resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Column(nullable = false)
    private Long lessonId;

    private String name;
    private String fileUrl;
    private String fileType;
    private Long sizeKb;
}