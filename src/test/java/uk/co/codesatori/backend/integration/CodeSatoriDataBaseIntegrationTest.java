package uk.co.codesatori.backend.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_2;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_4;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.codesatori.backend.CodeSatoriBackEndApplication;
import uk.co.codesatori.backend.model.Assignment;
import uk.co.codesatori.backend.model.AssignmentStatus;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.AssignmentRepository;
import uk.co.codesatori.backend.repositories.AssignmentStatusRepository;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.repositories.UserRepository;


@SpringBootTest(classes = CodeSatoriBackEndApplication.class)
public class CodeSatoriDataBaseIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClassOfStudentsRepository classOfStudentsRepository;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private AssignmentStatusRepository assignmentStatusRepository;

  private static User FIDDLE_STICKS = new User(
      UUID_1,
      "Fiddle",
      "Sticks",
      Role.TEACHER.value()
  );

  private static User THE_BANDITO = new User(
      UUID_2,
      "The",
      "Bandito",
      Role.STUDENT.value()
  );

  private static ClassOfStudents FIDDLE_STICKS_CLASS = new ClassOfStudents(
      "Mr Sticks' Class",
      UUID_1,
      Set.of(UUID_2),
      Collections.EMPTY_SET
  );

  private static Assignment FIDDLE_STICKS_ASSIGNMENT = new Assignment(
      "How to Stand Still",
      UUID_1,
      Collections.EMPTY_MAP,
      Collections.EMPTY_SET
  );


  private static AssignmentStatus FIDDLE_STICKS_ASSIGNMENT_STATUS = new AssignmentStatus(
      FIDDLE_STICKS_CLASS.getClassId(), FIDDLE_STICKS_ASSIGNMENT.getAssignmentId(), Timestamp.valueOf(LocalDateTime.now()), 0);

  @Test
  public void integratesSuccessfullyWithUserRepository() {
    /* Remove any existing users with conflicting UUID.
     * (These UUIDs are reserved for testing, so should not interfere with any actual data) */
    userRepository.delete(FIDDLE_STICKS);

    /* Demand a payload from said database and check to see that the request failed. */
    Optional<User> payload = userRepository.findById(FIDDLE_STICKS.getId());
    assertThat(payload.isEmpty()).isTrue();

    /* Save a dummy user to the actual database. */
    userRepository.save(FIDDLE_STICKS);

    /* Demand a payload from user database and check to see if it matches the user just saved. */
    payload = userRepository.findById(FIDDLE_STICKS.getId());
    assertThat(payload.isPresent()).isTrue();
    User result = payload.get();
    assertThat(result).isEqualTo(FIDDLE_STICKS);
  }

  @Test
  public void integratesSuccessfullyWithClassOfStudentsRepository() {
    /* Attempt to destroy data from previous integration tests. */
    StreamSupport
        .stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.getTeacherId().equals(FIDDLE_STICKS.getId()))
        .forEach(classOfStudents -> classOfStudentsRepository
            .deleteById(classOfStudents.getTeacherId()));

    /* Add a new class to the database. */
    classOfStudentsRepository.save(FIDDLE_STICKS_CLASS);

    /* Get all classes taught by the teacher of the class just saved. */
    List<ClassOfStudents> payload = StreamSupport
        .stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.getTeacherId().equals(FIDDLE_STICKS.getId()))
        .collect(Collectors.toList());

    /* Check that there is only one such class, i.e.: the one submitted. */
    assertThat(payload).hasSize(1);
    ClassOfStudents result = payload.get(0);
    FIDDLE_STICKS_CLASS.setClassId(result.getClassId());
    assertThat(result).isEqualTo(FIDDLE_STICKS_CLASS);

    /* Remove class from database and check to see that this operation has been successful. */
    classOfStudentsRepository.deleteById(result.getClassId());
    Optional<ClassOfStudents> emptyPayload = classOfStudentsRepository
        .findById(result.getClassId());
    assertThat(emptyPayload.isEmpty()).isTrue();
  }

  @Test
  public void integratesSuccessfullyWithAssignmentRepository() {
    /* Attempt to destroy data from previous integration tests. */
    StreamSupport
        .stream(assignmentRepository.findAll().spliterator(), false)
        .filter(assignment -> assignment.getTeacherId().equals(FIDDLE_STICKS.getId()))
        .forEach(assignment -> assignmentRepository
            .deleteById(assignment.getTeacherId()));

    /* Add a new assignment to the database. */
    assignmentRepository.save(FIDDLE_STICKS_ASSIGNMENT);

    /* Get all assignments set by the teacher of the assignment just saved. */
    List<Assignment> payload = StreamSupport
        .stream(assignmentRepository.findAll().spliterator(), false)
        .filter(assignment -> assignment.getTeacherId().equals(FIDDLE_STICKS.getId()))
        .collect(Collectors.toList());

    /* Check that there is only one such assignment, i.e.: the one submitted. */
    assertThat(payload).hasSize(1);
    Assignment result = payload.get(0);
    FIDDLE_STICKS_ASSIGNMENT.setAssignmentId(result.getAssignmentId());
    assertThat(result).isEqualTo(FIDDLE_STICKS_ASSIGNMENT);

    /* Remove assignment from database and check to see that this operation has been successful. */
    assignmentRepository.deleteById(result.getAssignmentId());
    Optional<Assignment> emptyPayload = assignmentRepository
        .findById(result.getAssignmentId());
    assertThat(emptyPayload.isEmpty()).isTrue();
  }
}


