package com.edulearn.assessment_service.repository;

import com.edulearn.assessment_service.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByStudentId(Long studentId);
    List<Attempt> findByStudentIdAndQuizId(Long studentId, Long quizId);
    long countByStudentIdAndQuizId(Long studentId, Long quizId);

    @Query("SELECT a FROM Attempt a WHERE a.studentId = :studentId AND a.quizId = :quizId ORDER BY a.score DESC")
    List<Attempt> findByStudentIdAndQuizIdOrderByScoreDesc(Long studentId, Long quizId);
}