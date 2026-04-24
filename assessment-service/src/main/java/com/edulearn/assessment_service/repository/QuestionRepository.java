package com.edulearn.assessment_service.repository;

import com.edulearn.assessment_service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuizIdOrderByOrderIndex(Long quizId);
    long countByQuizId(Long quizId);
    void deleteByQuizId(Long quizId);
}