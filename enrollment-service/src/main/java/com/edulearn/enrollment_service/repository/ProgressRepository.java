package com.edulearn.enrollment_service.repository;

import com.edulearn.enrollment_service.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

    List<Progress> findByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<Progress> findByStudentIdAndLessonId(Long studentId, Long lessonId);

    List<Progress> findByStudentId(Long studentId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.studentId = :studentId AND p.courseId = :courseId AND p.isCompleted = true")
    long countCompletedLessons(Long studentId, Long courseId);
}