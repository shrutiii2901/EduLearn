package com.edulearn.enrollment_service.service;

import com.edulearn.enrollment_service.entity.Certificate;
import com.edulearn.enrollment_service.entity.Enrollment;
import com.edulearn.enrollment_service.entity.Progress;
import com.edulearn.enrollment_service.repository.CertificateRepository;
import com.edulearn.enrollment_service.repository.EnrollmentRepository;
import com.edulearn.enrollment_service.repository.ProgressRepository;
import com.edulearn.enrollment_service.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepo;
    private final ProgressRepository progressRepo;
    private final CertificateRepository certificateRepo;

    // ── Enrollment ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        if (enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Student " + studentId + " is already enrolled in course " + courseId);
        }
        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .courseId(courseId)
                .status(Enrollment.Status.ACTIVE)
                .progressPercent(0.0)
                .build();
        return enrollmentRepo.save(enrollment);
    }

    @Override
    @Transactional
    public void unenroll(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepo.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setStatus(Enrollment.Status.CANCELLED);
        enrollmentRepo.save(enrollment);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepo.findByStudentId(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepo.findByCourseId(courseId);
    }

    @Override
    public boolean isEnrolled(Long studentId, Long courseId) {
        return enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public long getEnrollmentCount(Long courseId) {
        return enrollmentRepo.countByCourseId(courseId);
    }

    // ── Progress ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Progress trackProgress(Long studentId, Long courseId, Long lessonId, int watchedSeconds) {
        Progress progress = progressRepo.findByStudentIdAndLessonId(studentId, lessonId)
                .orElseGet(() -> Progress.builder()
                        .studentId(studentId)
                        .courseId(courseId)
                        .lessonId(lessonId)
                        .watchedSeconds(0)
                        .build());
        progress.setWatchedSeconds(progress.getWatchedSeconds() + watchedSeconds);
        return progressRepo.save(progress);
    }

    @Override
    @Transactional
    public Progress markLessonComplete(Long studentId, Long courseId, Long lessonId) {
        Progress progress = progressRepo.findByStudentIdAndLessonId(studentId, lessonId)
                .orElseGet(() -> Progress.builder()
                        .studentId(studentId)
                        .courseId(courseId)
                        .lessonId(lessonId)
                        .watchedSeconds(0)
                        .build());
        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        Progress saved = progressRepo.save(progress);

        // Update enrollment progress percentage - requires totalLessons from caller
        // Here we save the progress; the API caller should invoke computeCoursePercent
        return saved;
    }

    @Override
    public List<Progress> getCourseProgress(Long studentId, Long courseId) {
        return progressRepo.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public double computeCoursePercent(Long studentId, Long courseId, long totalLessons) {
        if (totalLessons == 0) return 0.0;
        long completed = progressRepo.countCompletedLessons(studentId, courseId);
        double percent = Math.min(100.0, (completed * 100.0) / totalLessons);

        // Sync enrollment record
        enrollmentRepo.findByStudentIdAndCourseId(studentId, courseId).ifPresent(e -> {
            e.setProgressPercent(percent);
            if (percent >= 100.0) {
                e.setStatus(Enrollment.Status.COMPLETED);
                e.setCompletedAt(LocalDateTime.now());
            }
            enrollmentRepo.save(e);
        });
        return percent;
    }

    // ── Certificate ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Certificate issueCertificate(Long studentId, Long courseId,
                                        String studentName, String courseName, String instructorName) {
        if (certificateRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Certificate already issued for this course.");
        }
        Enrollment enrollment = enrollmentRepo.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        if (enrollment.getProgressPercent() < 100.0) {
            throw new RuntimeException("Course not 100% complete. Current: " + enrollment.getProgressPercent() + "%");
        }

        Certificate cert = Certificate.builder()
                .studentId(studentId)
                .courseId(courseId)
                .studentName(studentName)
                .courseName(courseName)
                .instructorName(instructorName)
                .build();
        Certificate saved = certificateRepo.save(cert);

        enrollment.setCertificateIssued(true);
        enrollmentRepo.save(enrollment);
        return saved;
    }

    @Override
    public Certificate getCertificate(Long studentId, Long courseId) {
        return certificateRepo.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }

    @Override
    public Certificate verifyCertificate(String verificationCode) {
        return certificateRepo.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new RuntimeException("Invalid verification code: " + verificationCode));
    }

    @Override
    public List<Certificate> getCertificatesByStudent(Long studentId) {
        return certificateRepo.findByStudentId(studentId);
    }
}