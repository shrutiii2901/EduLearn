package com.edulearn.assessment_service.service;

import com.edulearn.assessment_service.entity.Attempt;
import com.edulearn.assessment_service.entity.Question;
import com.edulearn.assessment_service.entity.Quiz;
import java.util.List;
import java.util.Map;

public interface AssessmentService {

    
    Quiz createQuiz(Quiz quiz);
    Quiz getQuizById(Long quizId);
    List<Quiz> getQuizzesByCourse(Long courseId);
    List<Quiz> getPublishedQuizzesByCourse(Long courseId);
    Quiz updateQuiz(Long quizId, Quiz updated);
    void publishQuiz(Long quizId);
    void unpublishQuiz(Long quizId);
    void deleteQuiz(Long quizId);

    // ── Question ───────────────────────────────────────────────────────────────
    Question addQuestion(Question question);
    Question updateQuestion(Long questionId, Question updated);
    List<Question> getQuestionsByQuiz(Long quizId);
    void deleteQuestion(Long questionId);

    // ── Attempt ────────────────────────────────────────────────────────────────
    Attempt startAttempt(Long studentId, Long quizId);
    Attempt submitAttempt(Long attemptId, Map<Integer, String> answers);
    List<Attempt> getAttemptsByStudent(Long studentId);
    List<Attempt> getAttemptsForQuiz(Long studentId, Long quizId);
    Attempt getBestAttempt(Long studentId, Long quizId);
}