package com.edulearn.course_service.service;

import com.edulearn.course_service.entity.Course;
import com.edulearn.course_service.entity.Lesson;
import com.edulearn.course_service.entity.LessonResource;
import java.util.List;

public interface CourseService {

    // ── Course operations ──────────────────────────────────────────────────────
    Course createCourse(Course course);
    Course getCourseById(Long courseId);
    List<Course> getAllCourses();
    List<Course> getCoursesByCategory(String category);
    List<Course> getCoursesByInstructor(Long instructorId);
    List<Course> searchCourses(String keyword);
    List<Course> getFeaturedCourses();
    List<Course> getPendingCourses();
    Course updateCourse(Long courseId, Course updated);
    void publishCourse(Long courseId);
    void unpublishCourse(Long courseId);
    void approveCourse(Long courseId);
    void rejectCourse(Long courseId);
    void deleteCourse(Long courseId);

    // ── Lesson operations ──────────────────────────────────────────────────────
    Lesson addLesson(Lesson lesson);
    Lesson getLessonById(Long lessonId);
    List<Lesson> getLessonsByCourse(Long courseId);
    List<Lesson> getPreviewLessons(Long courseId);
    Lesson updateLesson(Long lessonId, Lesson updated);
    void deleteLesson(Long lessonId);
    long getLessonCount(Long courseId);

    // ── Resource operations ────────────────────────────────────────────────────
    LessonResource addResource(LessonResource resource);
    List<LessonResource> getResourcesByLesson(Long lessonId);
    void deleteResource(Long resourceId);
}