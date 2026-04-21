package com.edulearn.course_service.repository;

import com.edulearn.course_service.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseIdOrderByOrderIndex(Long courseId);

    List<Lesson> findByCourseIdAndIsPreviewTrue(Long courseId);

    long countByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);
}