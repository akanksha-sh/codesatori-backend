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
import uk.co.codesatori.backend.model.Assignment;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.AssignmentRepository;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

public class AssignmentControllerTest {

  @InjectMocks
  private AssignmentController assignmentController;

  @Mock
  private AssignmentRepository assignmentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SecurityService securityService;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  private static Assignment MR_WILLIAMS_ASSIGNMENT = new Assignment(
      "Mr Williams' Assignment",
      UUID_1,
      Collections.EMPTY_MAP,
      Collections.EMPTY_SET
  );

  private static Assignment MR_MACLEOD_ASSIGNMENT = new Assignment(
      "Mr Macleod's Assignment",
      UUID_2,
      Collections.EMPTY_MAP,
      Collections.EMPTY_SET
  );

  @Test
  public void getsAssignmentWithTheCorrectUUID() {
    when(securityService.verifyUserRole(Role.TEACHER, "This channel is for teachers only."))
        .thenReturn(MR_WILLIAMS.getId());
    when(userRepository.findById(MR_WILLIAMS.getId())).thenReturn(Optional.of(MR_WILLIAMS));
    when(assignmentRepository.findAll())
        .thenReturn(List.of(MR_WILLIAMS_ASSIGNMENT, MR_MACLEOD_ASSIGNMENT));

    List<Assignment> payload = assignmentController.getAssignmentsForTeacherDashboard();
    assertThat(payload).isEqualTo(List.of(MR_WILLIAMS_ASSIGNMENT));
  }

  @Test
  public void creatingNewAssignmentsSavesThemToRepo() {
    when(securityService.verifyUserRole(Role.TEACHER, "Only teachers can create assignments."))
        .thenReturn(MR_WILLIAMS.getId());
    when(userRepository.findById(MR_WILLIAMS.getId())).thenReturn(Optional.of(MR_WILLIAMS));

    Assignment payload = assignmentController.createNewAssignment(MR_WILLIAMS_ASSIGNMENT);
    verify(assignmentRepository).save(MR_WILLIAMS_ASSIGNMENT);
    assertThat(payload).isEqualTo(MR_WILLIAMS_ASSIGNMENT);
  }
}