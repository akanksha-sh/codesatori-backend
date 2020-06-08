package uk.co.codesatori.backend.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.codesatori.backend.CodeSatoriBackEndApplication;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.model.User.ROLE;
import uk.co.codesatori.backend.repositories.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CodeSatoriBackEndApplication.class)
//@ActiveProfiles("test")
public class CodeSatoriDataBaseIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  private static User FIDDLE_STICKS = new User(
      UUID_1,
      "Fiddle",
      "Sticks",
      ROLE.STUDENT.value()
  );

  @Test
  public void integratesSuccessfullyWithUserRepository() {
    /* Remove any existing user with conflicting UUID.
     * (This UUID is reserved for testing, so should not interfere with any actual accounts) */
    userRepository.delete(FIDDLE_STICKS);
    /* Demand a payload from said database and check to see that the request failed. */
    Optional<User> payload = userRepository.findById(FIDDLE_STICKS.getId());
    assertThat(payload.isEmpty()).isTrue();
    /* Save a dummy user to the actual database. */
    userRepository.save(FIDDLE_STICKS);
    /* Demand a payload from said database and check to see if it matches the user just saved. */
    payload = userRepository.findById(FIDDLE_STICKS.getId());
    assertThat(payload.isPresent()).isTrue();
    User result = payload.get();
    assertThat(result.getId()).isEqualTo(FIDDLE_STICKS.getId());
    assertThat(result.getFirstName()).isEqualTo(FIDDLE_STICKS.getFirstName());
    assertThat(result.getLastName()).isEqualTo(FIDDLE_STICKS.getLastName());
  }
}


