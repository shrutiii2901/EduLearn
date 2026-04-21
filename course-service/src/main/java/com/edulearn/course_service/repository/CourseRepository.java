package com.edulearn.course_service.repository;

import com.edulearn.course_service.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCategory(String category);

    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByLevel(Course.Level level);

    List<Course> findByIsPublishedTrue();

    List<Course> findByApprovalStatus(Course.ApprovalStatus status);

    List<Course> findByIsPublishedTrueOrderByCreatedAtDesc();

    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    List<Course> searchByKeyword(String keyword);
}