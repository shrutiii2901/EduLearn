package com.edulearn.enrollment_service.repository;

import com.edulearn.enrollment_service.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    long countByCourseId(Long courseId);

    List<Enrollment> findByStatus(Enrollment.Status status);
}