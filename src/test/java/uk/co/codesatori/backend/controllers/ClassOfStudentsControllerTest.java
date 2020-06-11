package uk.co.codesatori.backend.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.MR_WILLIAMS;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_2;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

public class ClassOfStudentsControllerTest {

  @InjectMocks
  private ClassOfStudentsController classOfStudentsController;

  @Mock
  private ClassOfStudentsRepository classOfStudentsRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SecurityService securityService;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  private static ClassOfStudents MR_WILLIAMS_CLASS = new ClassOfStudents(
      "Mr Williams' Class",
      UUID_1,
      true,
      Collections.EMPTY_SET,
      Collections.EMPTY_SET
  );

  private static ClassOfStudents MR_MACLEOD_CLASS = new ClassOfStudents(
      "Mr Macleod's Class",
      UUID_2,
      false,
      Collections.EMPTY_SET,
      Collections.EMPTY_SET
  );

  @Test
  public void getsClassWithTheCorrectUUID() {
    when(securityService.verifyUserRole(Role.TEACHER, "This channel is for teachers only."))
        .thenReturn(MR_WILLIAMS.getId());
    when(userRepository.findById(MR_WILLIAMS.getId())).thenReturn(Optional.of(MR_WILLIAMS));
    when(classOfStudentsRepository.findAll())
        .thenReturn(List.of(MR_WILLIAMS_CLASS, MR_MACLEOD_CLASS));

    List<ClassOfStudents> payload = classOfStudentsController
        .getClassesOfStudentsForTeacherDashboard();
    assertThat(payload).isEqualTo(List.of(MR_WILLIAMS_CLASS));
  }

  @Test
  public void creatingNewClassesSavesThemToRepo() {
    when(securityService.verifyUserRole(Role.TEACHER, "Only teachers can create classes."))
        .thenReturn(MR_WILLIAMS.getId());
    when(userRepository.findById(MR_WILLIAMS.getId())).thenReturn(Optional.of(MR_WILLIAMS));

    ClassOfStudents payload = classOfStudentsController.createNewClassOfStudents(MR_WILLIAMS_CLASS);
    verify(classOfStudentsRepository).save(MR_WILLIAMS_CLASS);
    assertThat(payload).isEqualTo(MR_WILLIAMS_CLASS);
  }
}