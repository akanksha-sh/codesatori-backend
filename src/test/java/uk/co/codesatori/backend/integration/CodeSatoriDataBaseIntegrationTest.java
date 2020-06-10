package uk.co.codesatori.backend.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_2;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_3;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import uk.co.codesatori.backend.CodeSatoriBackEndApplication;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.model.User.ROLE;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.repositories.UserRepository;


@SpringBootTest(classes = CodeSatoriBackEndApplication.class)
public class CodeSatoriDataBaseIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClassOfStudentsRepository classOfStudentsRepository;

  private static User FIDDLE_STICKS = new User(
      UUID_1,
      "Fiddle",
      "Sticks",
      ROLE.TEACHER.value()
  );

  private static User THE_BANDITO = new User(
      UUID_2,
      "The",
      "Bandito",
      ROLE.STUDENT.value()
  );

  private static ClassOfStudents FIDDLE_STICKS_CLASS = new ClassOfStudents(
      UUID_3,
      "Mr Sticks' Class",
      UUID_1,
      Set.of(UUID_2)
  );

  public <TData, TID> void integratesSuccessfullyWithRepository(
      CrudRepository<TData, TID> repository, TData datum, TID id) {
    /* Remove any existing data with conflicting UUID.
     * (These UUIDs are reserved for testing, so should not interfere with any actual data) */
    repository.delete(datum);

    /* Demand a payload from said database and check to see that the request failed. */
    Optional<TData> payload = repository.findById(id);
    assertThat(payload.isEmpty()).isTrue();

    /* Save a dummy datum to the actual database. */
    repository.save(datum);

    /* Demand a payload from said database and check to see if it matches the datum just saved. */
    payload = repository.findById(id);
    assertThat(payload.isPresent()).isTrue();
    TData result = payload.get();
    assertThat(result).isEqualTo(datum);
  }

  @Test
  public void integratesSuccessfullyWithUserRepository() {
    integratesSuccessfullyWithRepository(userRepository, FIDDLE_STICKS, FIDDLE_STICKS.getId());
  }

  @Test
  public void integratesSuccessfullyWithClassOfStudentsRepository() {
    integratesSuccessfullyWithRepository(classOfStudentsRepository, FIDDLE_STICKS_CLASS,
        FIDDLE_STICKS_CLASS.getClassId());
  }
}


