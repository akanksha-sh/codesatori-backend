package uk.co.codesatori.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.co.codesatori.backend.domain.Course;
import uk.co.codesatori.backend.domain.CourseRepository;

@RestController
public class OnlineClassController {

  private final CourseRepository courseRepository;

  @Autowired
  public OnlineClassController(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @GetMapping("/classes")
  public Iterable<Course> getOnlineClasses() {
    return courseRepository.findAll();
  }

  @PostMapping("/classes")
  void addOnlineClass(@RequestBody Course course) {
    courseRepository.save(course);
  }

  @PutMapping("/classes")
  void updateOnlineClass(@RequestBody Course course) {
    courseRepository.save(course);
  }

  @DeleteMapping("/classes/{id}")
  void deleteOnlineClass(@PathVariable Long id) {
    courseRepository.deleteById(id);
  }

}
