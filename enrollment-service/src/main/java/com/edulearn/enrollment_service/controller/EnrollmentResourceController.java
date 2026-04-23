package com.edulearn.enrollment_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edulearn.enrollment_service.entity.Certificate;
import com.edulearn.enrollment_service.entity.Enrollment;
import com.edulearn.enrollment_service.entity.Progress;
import com.edulearn.enrollment_service.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EnrollmentResourceController {

    private final EnrollmentService enrollmentService;

  
    // Enrollments apis

    @Tag(name = "Enrollment")
    @PostMapping("/enrollments")
    @Operation(summary = "Enroll a student into a course")
    public ResponseEntity<Enrollment> enroll(@RequestParam Long studentId,
                                             @RequestParam Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(studentId, courseId));
    }

    @Tag(name = "Enrollment")
    @DeleteMapping("/enrollments")
    @Operation(summary = "Unenroll a student from a course")
    public ResponseEntity<String> unenroll(@RequestParam Long studentId,
                                           @RequestParam Long courseId) {
        enrollmentService.unenroll(studentId, courseId);
        return ResponseEntity.ok("Unenrolled successfully.");
    }

    @Tag(name = "Enrollment")
    @GetMapping("/enrollments/student/{studentId}")
    @Operation(summary = "Get all enrollments for a student")
    public ResponseEntity<List<Enrollment>> byStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @Tag(name = "Enrollment")
    @GetMapping("/enrollments/course/{courseId}")
    @Operation(summary = "Get all enrollments for a course")
    public ResponseEntity<List<Enrollment>> byCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @Tag(name = "Enrollment")
    @GetMapping("/enrollments/check")
    @Operation(summary = "Check if a student is enrolled in a course")
    public ResponseEntity<Boolean> isEnrolled(@RequestParam Long studentId,
                                              @RequestParam Long courseId) {
        return ResponseEntity.ok(enrollmentService.isEnrolled(studentId, courseId));
    }

    @Tag(name = "Enrollment")
    @GetMapping("/enrollments/course/{courseId}/count")
    @Operation(summary = "Get total enrollment count for a course")
    public ResponseEntity<Long> count(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentCount(courseId));
    }

   //Progress apis

    @Tag(name = "Progress")
    @PostMapping("/progress/track")
    @Operation(summary = "Track watched seconds for a lesson")
    public ResponseEntity<Progress> trackProgress(@RequestParam Long studentId,
                                                  @RequestParam Long courseId,
                                                  @RequestParam Long lessonId,
                                                  @RequestParam int watchedSeconds) {
        return ResponseEntity.ok(enrollmentService.trackProgress(studentId, courseId, lessonId, watchedSeconds));
    }

    @Tag(name = "Progress")
    @PutMapping("/progress/complete")
    @Operation(summary = "Mark a lesson as completed")
    public ResponseEntity<Progress> complete(@RequestParam Long studentId,
                                             @RequestParam Long courseId,
                                             @RequestParam Long lessonId) {
        return ResponseEntity.ok(enrollmentService.markLessonComplete(studentId, courseId, lessonId));
    }

    @Tag(name = "Progress")
    @GetMapping("/progress/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get lesson-level progress for a course")
    public ResponseEntity<List<Progress>> getCourseProgress(@PathVariable Long studentId,
                                                            @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseProgress(studentId, courseId));
    }

    @Tag(name = "Progress")
    @GetMapping("/progress/student/{studentId}/course/{courseId}/percent")
    @Operation(summary = "Compute and get course completion percentage")
    public ResponseEntity<Double> getPercent(@PathVariable Long studentId,
                                             @PathVariable Long courseId,
                                             @RequestParam long totalLessons) {
        return ResponseEntity.ok(enrollmentService.computeCoursePercent(studentId, courseId, totalLessons));
    }

  //certificate apis

    @Tag(name = "Certificates")
    @PostMapping("/certificates/issue")
    @Operation(summary = "Issue certificate after 100% course completion")
    public ResponseEntity<Certificate> issue(@RequestParam Long studentId,
                                             @RequestParam Long courseId,
                                             @RequestParam String studentName,
                                             @RequestParam String courseName,
                                             @RequestParam String instructorName) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.issueCertificate(studentId, courseId, studentName, courseName, instructorName));
    }

    @Tag(name = "Certificates")
    @GetMapping("/certificates/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get certificate for a specific course")
    public ResponseEntity<Certificate> getCert(@PathVariable Long studentId,
                                               @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCertificate(studentId, courseId));
    }

    @Tag(name = "Certificates")
    @GetMapping("/certificates/student/{studentId}")
    @Operation(summary = "Get all certificates earned by a student")
    public ResponseEntity<List<Certificate>> allCerts(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getCertificatesByStudent(studentId));
    }

    @Tag(name = "Certificates")
    @GetMapping("/certificates/verify/{code}")
    @Operation(summary = "Publicly verify a certificate by its verification code (no login required)")
    public ResponseEntity<Certificate> verify(@PathVariable String code) {
        return ResponseEntity.ok(enrollmentService.verifyCertificate(code));
    }
}