package uk.co.codesatori.backend.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.MR_MACLEOD;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.MR_WILLIAMS;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_2;

import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.codesatori.backend.model.Teacher;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;

public class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserRepository userRepository;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  private static String UID_1 = "7dh28wud8b9s02DJWus9f7Wbd8S7";
  private static UUID UUID_1 = UUID.nameUUIDFromBytes(UID_1.getBytes());
  private static String UID_2 = "9sW7gb2hSkfWJD8uj3n583Shwb6G";
  private static UUID UUID_2 = UUID.nameUUIDFromBytes(UID_2.getBytes());

  private static User MR_WILLIAMS = new Teacher(
      UUID_1,
      "Knightsbridge College"
  );

  private static User MR_MACLEOD = new Teacher(
      UUID_2,
      "Kensington Secondary School"
  );

  @Test
  public void addingAndUpdatingUsersSavesThemToRepo() {
    userController.addUser(MR_WILLIAMS);
    userController.updateUser(MR_MACLEOD);

    verify(userRepository).save(MR_WILLIAMS);
    verify(userRepository).save(MR_MACLEOD);
  }

  @Test
  public void getsClassWithTheCorrectUUID() {
    when(userRepository.findById(UUID_1)).thenReturn(Optional.of(MR_WILLIAMS));
    when(userRepository.findById(UUID_2)).thenReturn(Optional.of(MR_MACLEOD));

    User payload1 = userController.getUser(UID_1);
    User payload2 = userController.getUser(UID_2);

    verify(userRepository).findById(UUID_1);
    verify(userRepository).findById(UUID_2);

    assertThat(payload1).isNotNull();
    assertThat(payload2).isNotNull();

    assertThat(payload1.getRole()).isEqualTo(MR_WILLIAMS.getRole());
    assertThat(payload1.getSchool()).isEqualTo(MR_WILLIAMS.getSchool());
    assertThat(payload2.getRole()).isEqualTo(MR_MACLEOD.getRole());
    assertThat(payload2.getSchool()).isEqualTo(MR_MACLEOD.getSchool());
  }

  @Test
  public void deletesClassWithTheCorrectUUID() {
    userController.deleteUser(UID_1);
    userController.deleteUser(UID_2);

    verify(userRepository).deleteById(UUID_1);
    verify(userRepository).deleteById(UUID_2);
  }
}