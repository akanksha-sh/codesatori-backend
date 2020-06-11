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
import uk.co.codesatori.backend.model.Assignment;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.AssignmentRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class AssignmentController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @GetMapping("/assignments/student")
  public List<Assignment> getAssignmentsForStudentDashboard() {
    /* Verify that request has come from a student. */
    UUID studentId = securityService
        .verifyUserRole(Role.STUDENT, "This channel is for students only.");
    /* Filter the database entries based on the given id. */
    return StreamSupport.stream(assignmentRepository.findAll().spliterator(), false)
        .filter(assignment -> assignment.getTeacherId().equals(studentId))
        .collect(Collectors.toList());
  }

  @GetMapping("/assignments/teacher")
  public List<Assignment> getAssignmentsForTeacherDashboard() {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "This channel is for teachers only.");
    /* Filter the database entries based on the given id. */
    return StreamSupport.stream(assignmentRepository.findAll().spliterator(), false)
        .filter(assignment -> assignment.getTeacherId().equals(teacherId))
        .collect(Collectors.toList());
  }

  @PostMapping("/assignments")
  public Assignment createNewAssignment(@RequestBody Assignment assignment) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can create assignments.");
    /* Add new assignment to the database and return to user. */
    assignment.setTeacherId(teacherId);
    assignmentRepository.save(assignment);
    return assignment;
  }
}