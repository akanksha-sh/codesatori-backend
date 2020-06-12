package uk.co.codesatori.backend.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.model.Assignment;
import uk.co.codesatori.backend.model.AssignmentStatus;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.AssignmentStatusRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class AssignmentStatusController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private AssignmentStatusRepository assignmentStatusRepository;
  
  @PostMapping("/assignment/publish")
  public AssignmentStatus publish(@RequestBody AssignmentStatus assignmentStatus) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can publish assignments.");
    /* Add new assignment to the database and return to user. */
    assignmentStatusRepository.save(assignmentStatus);
    return assignmentStatus;
  }
}
