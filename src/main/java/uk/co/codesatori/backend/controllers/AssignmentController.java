package uk.co.codesatori.backend.controllers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.*;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.AssignmentRepository;
import uk.co.codesatori.backend.repositories.StudentSubmissionRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class AssignmentController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private StudentSubmissionRepository studentSubmissionRepository;

  @GetMapping("/assignments/student")
  public List<AssignmentSubmissionPair> getAssignmentsForStudentDashboard() {
    /* Verify that request has come from a student. */
    UUID studentId = securityService
        .verifyUserRole(Role.STUDENT, "This channel is for students only.");
    /* Filter the database entries based on the given id. */

    return studentSubmissionRepository
            .findByStudentId(studentId)
            .stream().map(submission -> {
              Assignment assignment = assignmentRepository.findById(submission.getAssignmentId()).orElseThrow(IllegalStateException::new);
              return new AssignmentSubmissionPair(assignment, submission);
        }).collect(Collectors.toList());
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

  @DeleteMapping("/assignments/{id}")
  public void deleteAssignment(@PathVariable String id) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can create assignments.");
    /* Delete assignment from the database and return to user. */

    UUID assignmentId = UUID.fromString(id);

    List<UUID> assignmentIdsByTeacher =
        assignmentRepository
            .findByTeacherId(teacherId)
            .stream()
            .map(Assignment::getAssignmentId)
            .collect(Collectors.toList());

    if (!assignmentIdsByTeacher.contains(assignmentId)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Teacher does not own assignment he wishes to delete.");
    }

    assignmentRepository.deleteById(assignmentId);
  }

  @PutMapping("/assignments/edit/{assignmentIdString}")
  public Assignment editAssignment(@PathVariable String assignmentIdString, @RequestBody Assignment editAssignmentRequest) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can create assignments.");
    /* Check if assignmentId is owned by teacher, and then update. */

    UUID assignmentId = UUID.fromString(assignmentIdString);

    Assignment retrievedAssignment = assignmentRepository
        .findById(assignmentId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment does not exist."));

    if (!retrievedAssignment.getTeacherId().equals(teacherId)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Teacher does not own assignment he wishes to edit.");
    }

    retrievedAssignment.setAssignmentTemplate(editAssignmentRequest.getAssignmentTemplate());
    retrievedAssignment.setName(editAssignmentRequest.getName());
    assignmentRepository.save(retrievedAssignment);
    return retrievedAssignment;
  }

}