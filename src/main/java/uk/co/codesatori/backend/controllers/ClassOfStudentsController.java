package uk.co.codesatori.backend.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class ClassOfStudentsController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private ClassOfStudentsRepository classOfStudentsRepository;

  @GetMapping("/classes/student")
  public List<ClassOfStudents> getClassesOfStudentsForStudentDashboard() {
    /* Verify that request has come from a student. */
    UUID studentId = securityService
        .verifyUserRole(Role.STUDENT, "This channel is for students only.");
    /* Filter the database entries based on the given id. */
    return StreamSupport.stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.containsStudentWithId(studentId))
        .collect(Collectors.toList());
  }

  @GetMapping("/classes/teacher")
  public List<ClassOfStudents> getClassesOfStudentsForTeacherDashboard() {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "This channel is for teachers only.");
    /* Filter the database entries based on the given id. */
    return StreamSupport.stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.isTaughtByTeacherWithId(teacherId))
        .collect(Collectors.toList());
  }

  @PostMapping("/classes")
  public ClassOfStudents createNewClassOfStudents(@RequestBody ClassOfStudents classOfStudents) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can create classes.");
    /* Add new class to the database and return to user. */
    classOfStudents.setTeacherId(teacherId);
    classOfStudentsRepository.save(classOfStudents);
    return classOfStudents;
  }
}