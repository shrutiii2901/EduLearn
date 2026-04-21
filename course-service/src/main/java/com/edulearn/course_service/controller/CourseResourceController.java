package com.edulearn.course_service.controller;

import com.edulearn.course_service.entity.Course;
import com.edulearn.course_service.entity.Lesson;
import com.edulearn.course_service.entity.LessonResource;
import com.edulearn.course_service.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CourseResourceController {

    private final CourseService courseService;
   
    // ════════════════════════════════════════════════════════════════════════════
    // COURSE ENDPOINTS
    // ════════════════════════════════════════════════════════════════════════════

    @Tag(name = "Courses")
    @PostMapping("/courses")
    @Operation(summary = "Create a new course (Instructor)")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(course));
    }

    @Tag(name = "Courses")
    @GetMapping("/courses")
    @Operation(summary = "Get all courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Tag(name = "Courses")
    @GetMapping("/courses/featured")
    @Operation(summary = "Get featured (published) courses for home page")
    public ResponseEntity<List<Course>> getFeatured() {
        return ResponseEntity.ok(courseService.getFeaturedCourses());
    }

    @Tag(name = "Courses")
    @GetMapping("/courses/pending")
    @Operation(summary = "Get courses pending admin approval")
    public ResponseEntity<List<Course>> getPending() {
        return ResponseEntity.ok(courseService.getPendingCourses());
    }

    @Tag(name = "Courses")
    @GetMapping("/courses/{courseId}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<Course> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @Tag(name = "Courses")
    @GetMapping("/courses/search")
    @Operation(summary = "Search courses by keyword")
    public ResponseEntity<List<Course>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }

    @Tag(name = "Courses")
    @GetMapping("/courses/category")
    @Operation(summary = "Get courses by category")
    public ResponseEntity<List<Course>> byCategory(@RequestParam String category) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(category));
    }

    @Tag(name = "Courses")
    @GetMapping("/courses/instructor/{instructorId}")
    @Operation(summary = "Get courses by instructor")
    public ResponseEntity<List<Course>> byInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    @Tag(name = "Courses")
    @PutMapping("/courses/{courseId}")
    @Operation(summary = "Update course details (Instructor)")
    public ResponseEntity<Course> updateCourse(@PathVariable Long courseId, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, course));
    }

    @Tag(name = "Courses")
    @PutMapping("/courses/{courseId}/publish")
    @Operation(summary = "Publish course (Instructor) — requires prior Admin approval")
    public ResponseEntity<String> publish(@PathVariable Long courseId) {
        courseService.publishCourse(courseId);
        return ResponseEntity.ok("Course published successfully.");
    }

    @Tag(name = "Courses")
    @PutMapping("/courses/{courseId}/unpublish")
    @Operation(summary = "Unpublish course (Instructor)")
    public ResponseEntity<String> unpublish(@PathVariable Long courseId) {
        courseService.unpublishCourse(courseId);
        return ResponseEntity.ok("Course unpublished.");
    }

    @Tag(name = "Courses")
    @PutMapping("/courses/{courseId}/approve")
    @Operation(summary = "Approve course (Admin only)")
    public ResponseEntity<String> approve(@PathVariable Long courseId) {
        courseService.approveCourse(courseId);
        return ResponseEntity.ok("Course approved.");
    }

    @Tag(name = "Courses")
    @PutMapping("/courses/{courseId}/reject")
    @Operation(summary = "Reject course (Admin only)")
    public ResponseEntity<String> reject(@PathVariable Long courseId) {
        courseService.rejectCourse(courseId);
        return ResponseEntity.ok("Course rejected.");
    }

    @Tag(name = "Courses")
    @DeleteMapping("/courses/{courseId}")
    @Operation(summary = "Delete course and all its lessons (Instructor/Admin)")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // LESSON ENDPOINTS
    // ════════════════════════════════════════════════════════════════════════════

    @Tag(name = "Lessons")
    @PostMapping("/lessons")
    @Operation(summary = "Add a lesson to a course (Instructor)")
    public ResponseEntity<Lesson> addLesson(@RequestBody Lesson lesson) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addLesson(lesson));
    }

    @Tag(name = "Lessons")
    @GetMapping("/lessons/{lessonId}")
    @Operation(summary = "Get lesson by ID")
    public ResponseEntity<Lesson> getLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(courseService.getLessonById(lessonId));
    }

    @Tag(name = "Lessons")
    @GetMapping("/lessons/course/{courseId}")
    @Operation(summary = "Get all lessons of a course (ordered)")
    public ResponseEntity<List<Lesson>> getLessonsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getLessonsByCourse(courseId));
    }

    @Tag(name = "Lessons")
    @GetMapping("/lessons/course/{courseId}/preview")
    @Operation(summary = "Get free preview lessons of a course")
    public ResponseEntity<List<Lesson>> getPreviewLessons(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getPreviewLessons(courseId));
    }

    @Tag(name = "Lessons")
    @GetMapping("/lessons/course/{courseId}/count")
    @Operation(summary = "Get total lesson count for a course")
    public ResponseEntity<Long> getLessonCount(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getLessonCount(courseId));
    }

    @Tag(name = "Lessons")
    @PutMapping("/lessons/{lessonId}")
    @Operation(summary = "Update lesson (Instructor)")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long lessonId, @RequestBody Lesson lesson) {
        return ResponseEntity.ok(courseService.updateLesson(lessonId, lesson));
    }

    @Tag(name = "Lessons")
    @DeleteMapping("/lessons/{lessonId}")
    @Operation(summary = "Delete lesson and its resources (Instructor)")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        courseService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // RESOURCE ENDPOINTS
    // ════════════════════════════════════════════════════════════════════════════

    @Tag(name = "Resources")
    @PostMapping("/resources")
    @Operation(summary = "Add a downloadable resource to a lesson (Instructor)")
    public ResponseEntity<LessonResource> addResource(@RequestBody LessonResource resource) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addResource(resource));
    }

    @Tag(name = "Resources")
    @GetMapping("/resources/lesson/{lessonId}")
    @Operation(summary = "Get all resources for a lesson")
    public ResponseEntity<List<LessonResource>> getResources(@PathVariable Long lessonId) {
        return ResponseEntity.ok(courseService.getResourcesByLesson(lessonId));
    }

    @Tag(name = "Resources")
    @DeleteMapping("/resources/{resourceId}")
    @Operation(summary = "Delete a resource (Instructor)")
    public ResponseEntity<Void> deleteResource(@PathVariable Long resourceId) {
        courseService.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }
}