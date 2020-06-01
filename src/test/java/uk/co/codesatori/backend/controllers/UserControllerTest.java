package uk.co.codesatori.backend.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

  private static UUID UUID_1 = UUID.fromString("449cefa5-47cd-4777-adbd-5653b051ef5a");
  private static UUID UUID_2 = UUID.fromString("33123be4-1423-483b-80ae-b558d04d6008");

  private static User MR_WILLIAMS = new Teacher(
      UUID_1,
      "mrwilliams",
      "password1"
  );

  private static User MR_MACLEOD = new Teacher(
      UUID_2,
      "mrmacleod",
      "password2"
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

    User payload1 = userController.getUser(UUID_1);
    User payload2 = userController.getUser(UUID_2);

    verify(userRepository).findById(UUID_1);
    verify(userRepository).findById(UUID_2);

    assertThat(payload1).isNotNull();
    assertThat(payload2).isNotNull();

    assertThat(payload1.getUsername()).isEqualTo(MR_WILLIAMS.getUsername());
    assertThat(payload1.getPassword()).isEqualTo(MR_WILLIAMS.getPassword());
    assertThat(payload2.getUsername()).isEqualTo(MR_MACLEOD.getUsername());
    assertThat(payload2.getPassword()).isEqualTo(MR_MACLEOD.getPassword());
  }

  @Test
  public void deletesClassWithTheCorrectUUID() {
    userController.deleteUser(UUID_1);
    userController.deleteUser(UUID_2);

    verify(userRepository).deleteById(UUID_1);
    verify(userRepository).deleteById(UUID_2);
  }
}