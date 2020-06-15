package uk.co.codesatori.backend.controllers;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.Assignment;
import uk.co.codesatori.backend.model.AssignmentStatus;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.AssignmentStatusRepository;
import uk.co.codesatori.backend.repositories.StudentSubmissionRepository;
import uk.co.codesatori.backend.security.SecurityService;
import uk.co.codesatori.backend.service.AssignmentStatusService;

@RestController
public class AssignmentStatusController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private AssignmentStatusService assignmentStatusService;
  
  @PostMapping("/assignments/publish")
  public AssignmentStatus publish(@RequestBody AssignmentStatus assignmentStatus) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can publish assignments.");
    /* Add new assignment to the database and return to user. */
    Optional<AssignmentStatus> output = assignmentStatusService.publish(assignmentStatus);
    if (output.isPresent()) {
      return output.get();
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Teacher either does not own this assignment, or does not have this class");
    }
  }
}
