package com.edulearn.enrollment_service.service;

import com.edulearn.enrollment_service.entity.Certificate;
import com.edulearn.enrollment_service.entity.Enrollment;
import com.edulearn.enrollment_service.entity.Progress;
import java.util.List;

public interface EnrollmentService {

    // ── Enrollment ─────────────────────────────────────────────────────────────
    Enrollment enroll(Long studentId, Long courseId);
    void unenroll(Long studentId, Long courseId);
    List<Enrollment> getEnrollmentsByStudent(Long studentId);
    List<Enrollment> getEnrollmentsByCourse(Long courseId);
    boolean isEnrolled(Long studentId, Long courseId);
    long getEnrollmentCount(Long courseId);

    // ── Progress ───────────────────────────────────────────────────────────────
    Progress trackProgress(Long studentId, Long courseId, Long lessonId, int watchedSeconds);
    Progress markLessonComplete(Long studentId, Long courseId, Long lessonId);
    List<Progress> getCourseProgress(Long studentId, Long courseId);
    double computeCoursePercent(Long studentId, Long courseId, long totalLessons);

    // ── Certificate ────────────────────────────────────────────────────────────
    Certificate issueCertificate(Long studentId, Long courseId,
                                 String studentName, String courseName, String instructorName);
    Certificate getCertificate(Long studentId, Long courseId);
    Certificate verifyCertificate(String verificationCode);
    List<Certificate> getCertificatesByStudent(Long studentId);
}