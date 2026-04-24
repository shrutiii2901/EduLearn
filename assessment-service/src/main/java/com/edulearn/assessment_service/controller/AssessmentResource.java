package com.edulearn.assessment_service.controller;

import com.edulearn.assessment_service.entity.Attempt;
import com.edulearn.assessment_service.entity.Question;
import com.edulearn.assessment_service.entity.Quiz;
import com.edulearn.assessment_service.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AssessmentResource {

    private final AssessmentService assessmentService;

    // ════════════════════════════════════════════════════════════════════════════
    // QUIZ ENDPOINTS
    // ════════════════════════════════════════════════════════════════════════════

    @Tag(name = "Quizzes")
    @PostMapping("/quizzes")
    @Operation(summary = "Create a quiz for a course (Instructor)")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assessmentService.createQuiz(quiz));
    }

    @Tag(name = "Quizzes")
    @GetMapping("/quizzes/{quizId}")
    @Operation(summary = "Get quiz by ID")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(assessmentService.getQuizById(quizId));
    }

    @Tag(name = "Quizzes")
    @GetMapping("/quizzes/course/{courseId}")
    @Operation(summary = "Get all quizzes for a course (Instructor view)")
    public ResponseEntity<List<Quiz>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(assessmentService.getQuizzesByCourse(courseId));
    }

    @Tag(name = "Quizzes")
    @GetMapping("/quizzes/course/{courseId}/published")
    @Operation(summary = "Get published quizzes for a course (Student view)")
    public ResponseEntity<List<Quiz>> getPublished(@PathVariable Long courseId) {
        return ResponseEntity.ok(assessmentService.getPublishedQuizzesByCourse(courseId));
    }

    @Tag(name = "Quizzes")
    @PutMapping("/quizzes/{quizId}")
    @Operation(summary = "Update quiz details (Instructor)")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz quiz) {
        return ResponseEntity.ok(assessmentService.updateQuiz(quizId, quiz));
    }

    @Tag(name = "Quizzes")
    @PutMapping("/quizzes/{quizId}/publish")
    @Operation(summary = "Publish quiz so students can attempt it (Instructor)")
    public ResponseEntity<String> publishQuiz(@PathVariable Long quizId) {
        assessmentService.publishQuiz(quizId);
        return ResponseEntity.ok("Quiz published successfully.");
    }

    @Tag(name = "Quizzes")
    @PutMapping("/quizzes/{quizId}/unpublish")
    @Operation(summary = "Unpublish quiz (Instructor)")
    public ResponseEntity<String> unpublishQuiz(@PathVariable Long quizId) {
        assessmentService.unpublishQuiz(quizId);
        return ResponseEntity.ok("Quiz unpublished.");
    }

    @Tag(name = "Quizzes")
    @DeleteMapping("/quizzes/{quizId}")
    @Operation(summary = "Delete quiz and all its questions (Instructor)")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        assessmentService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // QUESTION ENDPOINTS
    // ════════════════════════════════════════════════════════════════════════════

    @Tag(name = "Questions")
    @PostMapping("/questions")
    @Operation(summary = "Add a question to a quiz (Instructor)")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assessmentService.addQuestion(question));
    }

    @Tag(name = "Questions")
    @GetMapping("/questions/quiz/{quizId}")
    @Operation(summary = "Get all questions for a quiz (ordered)")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable Long quizId) {
        return ResponseEntity.ok(assessmentService.getQuestionsByQuiz(quizId));
    }

    @Tag(name = "Questions")
    @PutMapping("/questions/{questionId}")
    @Operation(summary = "Update a question (Instructor)")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long questionId,
                                                   @RequestBody Question question) {
        return ResponseEntity.ok(assessmentService.updateQuestion(questionId, question));
    }

    @Tag(name = "Questions")
    @DeleteMapping("/questions/{questionId}")
    @Operation(summary = "Delete a question (Instructor)")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        assessmentService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // ATTEMPT ENDPOINTS
    // ════════════════════════════════════════════════════════════════════════════

    @Tag(name = "Attempts")
    @PostMapping("/attempts/start")
    @Operation(summary = "Start a quiz attempt (Student) — checks attempt limit")
    public ResponseEntity<Attempt> startAttempt(@RequestParam Long studentId,
                                                @RequestParam Long quizId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assessmentService.startAttempt(studentId, quizId));
    }

    @Tag(name = "Attempts")
    @PostMapping("/attempts/{attemptId}/submit")
    @Operation(summary = "Submit answers and get auto-graded result (Student)")
    public ResponseEntity<Attempt> submitAttempt(@PathVariable Long attemptId,
                                                 @RequestBody Map<Integer, String> answers) {
        return ResponseEntity.ok(assessmentService.submitAttempt(attemptId, answers));
    }

    @Tag(name = "Attempts")
    @GetMapping("/attempts/student/{studentId}")
    @Operation(summary = "Get all attempts by a student across all quizzes")
    public ResponseEntity<List<Attempt>> byStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(assessmentService.getAttemptsByStudent(studentId));
    }

    @Tag(name = "Attempts")
    @GetMapping("/attempts/student/{studentId}/quiz/{quizId}")
    @Operation(summary = "Get all attempts by a student for a specific quiz")
    public ResponseEntity<List<Attempt>> forQuiz(@PathVariable Long studentId,
                                                 @PathVariable Long quizId) {
        return ResponseEntity.ok(assessmentService.getAttemptsForQuiz(studentId, quizId));
    }

    @Tag(name = "Attempts")
    @GetMapping("/attempts/student/{studentId}/quiz/{quizId}/best")
    @Operation(summary = "Get best scoring attempt by a student for a quiz")
    public ResponseEntity<Attempt> bestAttempt(@PathVariable Long studentId,
                                               @PathVariable Long quizId) {
        return ResponseEntity.ok(assessmentService.getBestAttempt(studentId, quizId));
    }
}