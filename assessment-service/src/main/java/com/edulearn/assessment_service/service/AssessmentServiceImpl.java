package com.edulearn.assessment_service.service;

import com.edulearn.assessment_service.entity.Attempt;
import com.edulearn.assessment_service.entity.Question;
import com.edulearn.assessment_service.entity.Quiz;
import com.edulearn.assessment_service.repository.AttemptRepository;
import com.edulearn.assessment_service.repository.QuestionRepository;
import com.edulearn.assessment_service.repository.QuizRepository;
import com.edulearn.assessment_service.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final AttemptRepository attemptRepo;

    // ── Quiz ───────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        return quizRepo.save(quiz);
    }

    @Override
    public Quiz getQuizById(Long quizId) {
        return quizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));
    }

    @Override
    public List<Quiz> getQuizzesByCourse(Long courseId) {
        return quizRepo.findByCourseId(courseId);
    }

    @Override
    public List<Quiz> getPublishedQuizzesByCourse(Long courseId) {
        return quizRepo.findByCourseIdAndIsPublishedTrue(courseId);
    }

    @Override
    @Transactional
    public Quiz updateQuiz(Long quizId, Quiz updated) {
        Quiz quiz = getQuizById(quizId);
        if (updated.getTitle() != null)            quiz.setTitle(updated.getTitle());
        if (updated.getDescription() != null)      quiz.setDescription(updated.getDescription());
        if (updated.getTimeLimitMinutes() != null) quiz.setTimeLimitMinutes(updated.getTimeLimitMinutes());
        if (updated.getPassingScore() != null)     quiz.setPassingScore(updated.getPassingScore());
        if (updated.getMaxAttempts() != null)      quiz.setMaxAttempts(updated.getMaxAttempts());
        return quizRepo.save(quiz);
    }

    @Override
    @Transactional
    public void publishQuiz(Long quizId) {
        Quiz quiz = getQuizById(quizId);
        if (questionRepo.countByQuizId(quizId) == 0) {
            throw new RuntimeException("Cannot publish a quiz with no questions.");
        }
        quiz.setPublished(true);
        quizRepo.save(quiz);
    }

    @Override
    @Transactional
    public void unpublishQuiz(Long quizId) {
        Quiz quiz = getQuizById(quizId);
        quiz.setPublished(false);
        quizRepo.save(quiz);
    }

    @Override
    @Transactional
    public void deleteQuiz(Long quizId) {
        questionRepo.deleteByQuizId(quizId);
        quizRepo.deleteById(quizId);
    }

    // ── Question ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Question addQuestion(Question question) {
        if (question.getOrderIndex() == null) {
            long count = questionRepo.countByQuizId(question.getQuizId());
            question.setOrderIndex((int) count + 1);
        }
        return questionRepo.save(question);
    }

    @Override
    @Transactional
    public Question updateQuestion(Long questionId, Question updated) {
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));
        if (updated.getText() != null)          q.setText(updated.getText());
        if (updated.getType() != null)          q.setType(updated.getType());
        if (updated.getOptions() != null)       q.setOptions(updated.getOptions());
        if (updated.getCorrectAnswer() != null) q.setCorrectAnswer(updated.getCorrectAnswer());
        if (updated.getMarks() != null)         q.setMarks(updated.getMarks());
        if (updated.getOrderIndex() != null)    q.setOrderIndex(updated.getOrderIndex());
        return questionRepo.save(q);
    }

    @Override
    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepo.findByQuizIdOrderByOrderIndex(quizId);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long questionId) {
        questionRepo.deleteById(questionId);
    }

    // ── Attempt ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Attempt startAttempt(Long studentId, Long quizId) {
        Quiz quiz = getQuizById(quizId);

        if (!quiz.isPublished()) {
            throw new RuntimeException("Quiz is not published yet.");
        }

        long attemptCount = attemptRepo.countByStudentIdAndQuizId(studentId, quizId);
        if (attemptCount >= quiz.getMaxAttempts()) {
            throw new RuntimeException("Maximum attempt limit (" + quiz.getMaxAttempts() + ") reached for this quiz.");
        }

        Attempt attempt = Attempt.builder()
                .studentId(studentId)
                .quizId(quizId)
                .answers(new HashMap<>())
                .build();
        return attemptRepo.save(attempt);
    }

    @Override
    @Transactional
    public Attempt submitAttempt(Long attemptId, Map<Integer, String> answers) {
        Attempt attempt = attemptRepo.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found: " + attemptId));

        if (attempt.getSubmittedAt() != null) {
            throw new RuntimeException("This attempt has already been submitted.");
        }

        Quiz quiz = getQuizById(attempt.getQuizId());
        List<Question> questions = questionRepo.findByQuizIdOrderByOrderIndex(attempt.getQuizId());

        // Auto-grade: compare each answer against correct answer (case-insensitive)
        double totalMarks = 0.0;
        double earnedMarks = 0.0;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            totalMarks += q.getMarks();
            String studentAnswer = answers.getOrDefault(i + 1, "").trim();
            if (q.getCorrectAnswer().equalsIgnoreCase(studentAnswer)) {
                earnedMarks += q.getMarks();
            }
        }

        double scorePercent = totalMarks > 0 ? (earnedMarks / totalMarks) * 100.0 : 0.0;

        attempt.setAnswers(answers);
        attempt.setScore(Math.round(scorePercent * 100.0) / 100.0);
        attempt.setPassed(scorePercent >= quiz.getPassingScore());
        attempt.setSubmittedAt(LocalDateTime.now());

        return attemptRepo.save(attempt);
    }

    @Override
    public List<Attempt> getAttemptsByStudent(Long studentId) {
        return attemptRepo.findByStudentId(studentId);
    }

    @Override
    public List<Attempt> getAttemptsForQuiz(Long studentId, Long quizId) {
        return attemptRepo.findByStudentIdAndQuizId(studentId, quizId);
    }

    @Override
    public Attempt getBestAttempt(Long studentId, Long quizId) {
        List<Attempt> attempts = attemptRepo.findByStudentIdAndQuizIdOrderByScoreDesc(studentId, quizId);
        if (attempts.isEmpty()) {
            throw new RuntimeException("No attempts found for student " + studentId + " on quiz " + quizId);
        }
        return attempts.get(0);
    }
}