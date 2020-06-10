package uk.co.codesatori.backend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.model.User.ROLE;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class ClassOfStudentsController {

  @Autowired
  SecurityService securityService;

  @Autowired
  private ClassOfStudentsRepository classOfStudentsRepository;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/classes")
  public List<ClassOfStudents> getClassOfStudentsByTeacher() {
    /* Get UUID for the user who made this request.
     * Then probe the user details database to check that the user is actually a teacher. */
    UUID teacherId = securityService.getCurrentUUID();
    Optional<User> user = userRepository.findById(teacherId);

    /* If the user does not exist in the database, or is not a teacher, throw an error. */
    if (user.isEmpty() || !user.get().getRoleAsEnum().equals(ROLE.TEACHER)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Only teachers can create classes.");
    }

    /* Otherwise, filter the database entries based on the given id. */
    return StreamSupport.stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.getTeacherId().equals(teacherId))
        .collect(Collectors.toList());
  }
}
