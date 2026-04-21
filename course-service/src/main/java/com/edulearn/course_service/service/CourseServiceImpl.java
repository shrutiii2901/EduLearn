package com.edulearn.course_service.service;

import com.edulearn.course_service.entity.Course;
import com.edulearn.course_service.entity.Lesson;
import com.edulearn.course_service.entity.LessonResource;
import com.edulearn.course_service.repository.CourseRepository;
import com.edulearn.course_service.repository.LessonRepository;
import com.edulearn.course_service.repository.LessonResourceRepository;
import com.edulearn.course_service.service.CourseService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepo;
    private final LessonRepository lessonRepo;
    private final LessonResourceRepository resourceRepo;

//    public CourseServiceImpl(CourseRepository courseRepo,
//                          LessonRepository lessonRepo,
//                          LessonResourceRepository resourceRepo) {
//     this.courseRepo = courseRepo;
//     this.lessonRepo = lessonRepo;
//     this.resourceRepo = resourceRepo;
// }

    // ── Course operations ──────────────────────────────────────────────────────

    @Override
    @Transactional
    public Course createCourse(Course course) {
        return courseRepo.save(course);
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    @Override
    public List<Course> getCoursesByCategory(String category) {
        return courseRepo.findByCategory(category);
    }

    @Override
    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepo.findByInstructorId(instructorId);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        return courseRepo.searchByKeyword(keyword);
    }

    @Override
    public List<Course> getFeaturedCourses() {
        return courseRepo.findByIsPublishedTrueOrderByCreatedAtDesc();
    }

    @Override
    public List<Course> getPendingCourses() {
        return courseRepo.findByApprovalStatus(Course.ApprovalStatus.PENDING);
    }

    @Override
    @Transactional
    public Course updateCourse(Long courseId, Course updated) {
        Course course = getCourseById(courseId);
        if (updated.getTitle() != null)              course.setTitle(updated.getTitle());
        if (updated.getDescription() != null)        course.setDescription(updated.getDescription());
        if (updated.getCategory() != null)           course.setCategory(updated.getCategory());
        if (updated.getPrice() != null)              course.setPrice(updated.getPrice());
        if (updated.getLevel() != null)              course.setLevel(updated.getLevel());
        if (updated.getLanguage() != null)           course.setLanguage(updated.getLanguage());
        if (updated.getThumbnailUrl() != null)       course.setThumbnailUrl(updated.getThumbnailUrl());
        if (updated.getTotalDurationMinutes() != null) course.setTotalDurationMinutes(updated.getTotalDurationMinutes());
        return courseRepo.save(course);
    }

    @Override
    @Transactional
    public void publishCourse(Long courseId) {
        Course course = getCourseById(courseId);
        if (course.getApprovalStatus() != Course.ApprovalStatus.APPROVED) {
            throw new RuntimeException("Course must be approved by Admin before it can be published.");
        }
        course.setPublished(true);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void unpublishCourse(Long courseId) {
        Course course = getCourseById(courseId);
        course.setPublished(false);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void approveCourse(Long courseId) {
        Course course = getCourseById(courseId);
        course.setApprovalStatus(Course.ApprovalStatus.APPROVED);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void rejectCourse(Long courseId) {
        Course course = getCourseById(courseId);
        course.setApprovalStatus(Course.ApprovalStatus.REJECTED);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        // Also delete all associated lessons and their resources
        List<Lesson> lessons = lessonRepo.findByCourseIdOrderByOrderIndex(courseId);
        for (Lesson lesson : lessons) {
            resourceRepo.deleteByLessonId(lesson.getLessonId());
        }
        lessonRepo.deleteByCourseId(courseId);
        courseRepo.deleteById(courseId);
    }

    // ── Lesson operations ──────────────────────────────────────────────────────

    @Override
    @Transactional
    public Lesson addLesson(Lesson lesson) {
        // Auto-assign orderIndex if not provided
        if (lesson.getOrderIndex() == null) {
            long count = lessonRepo.countByCourseId(lesson.getCourseId());
            lesson.setOrderIndex((int) count + 1);
        }
        return lessonRepo.save(lesson);
    }

    @Override
    public Lesson getLessonById(Long lessonId) {
        return lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
    }

    @Override
    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepo.findByCourseIdOrderByOrderIndex(courseId);
    }

    @Override
    public List<Lesson> getPreviewLessons(Long courseId) {
        return lessonRepo.findByCourseIdAndIsPreviewTrue(courseId);
    }

    @Override
    @Transactional
    public Lesson updateLesson(Long lessonId, Lesson updated) {
        Lesson lesson = getLessonById(lessonId);
        if (updated.getTitle() != null)           lesson.setTitle(updated.getTitle());
        if (updated.getDescription() != null)     lesson.setDescription(updated.getDescription());
        if (updated.getContentUrl() != null)      lesson.setContentUrl(updated.getContentUrl());
        if (updated.getContentType() != null)     lesson.setContentType(updated.getContentType());
        if (updated.getDurationMinutes() != null) lesson.setDurationMinutes(updated.getDurationMinutes());
        if (updated.getOrderIndex() != null)      lesson.setOrderIndex(updated.getOrderIndex());
        lesson.setPreview(updated.isPreview());
        return lessonRepo.save(lesson);
    }

    @Override
    @Transactional
    public void deleteLesson(Long lessonId) {
        resourceRepo.deleteByLessonId(lessonId);
        lessonRepo.deleteById(lessonId);
    }

    @Override
    public long getLessonCount(Long courseId) {
        return lessonRepo.countByCourseId(courseId);
    }

    // ── Resource operations ────────────────────────────────────────────────────

    @Override
    @Transactional
    public LessonResource addResource(LessonResource resource) {
        return resourceRepo.save(resource);
    }

    @Override
    public List<LessonResource> getResourcesByLesson(Long lessonId) {
        return resourceRepo.findByLessonId(lessonId);
    }

    @Override
    @Transactional
    public void deleteResource(Long resourceId) {
        resourceRepo.deleteById(resourceId);
    }
}