package uk.co.codesatori.backend.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.FIREBASE_UUID_1;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.FIREBASE_UUID_2;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.MR_MACLEOD;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.MR_WILLIAMS;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_2;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

public class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SecurityService securityService;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void signingUpNewUsersSavesThemToRepo() {
    when(securityService.getCurrentUUID()).thenReturn(MR_WILLIAMS.getId());

    userController.signUpNewUser(MR_WILLIAMS);
    verify(userRepository).save(MR_WILLIAMS);
  }

  @Test
  public void getsClassWithTheCorrectUUID() {
    when(userRepository.findById(UUID_1)).thenReturn(Optional.of(MR_WILLIAMS));
    when(userRepository.findById(UUID_2)).thenReturn(Optional.of(MR_MACLEOD));

    User payload1 = userController.getUser(FIREBASE_UUID_1);
    User payload2 = userController.getUser(FIREBASE_UUID_2);

    verify(userRepository).findById(UUID_1);
    verify(userRepository).findById(UUID_2);

    assertThat(payload1).isNotNull();
    assertThat(payload2).isNotNull();

    assertThat(payload1).isEqualTo(MR_WILLIAMS);
    assertThat(payload2).isEqualTo(MR_MACLEOD);
  }

//  @Test
//  public void deletesClassWithTheCorrectUUID() {
//    userController.deleteUser(FIREBASE_UUID_1);
//    userController.deleteUser(FIREBASE_UUID_2);
//
//    verify(userRepository).deleteById(UUID_1);
//    verify(userRepository).deleteById(UUID_2);
//  }
}