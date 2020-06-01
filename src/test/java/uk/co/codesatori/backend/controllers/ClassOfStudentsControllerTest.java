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
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;

public class ClassOfStudentsControllerTest {

  @InjectMocks
  private ClassOfStudentsController classOfStudentsController;

  @Mock
  private ClassOfStudentsRepository classOfStudentsRepository;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  private static UUID UUID_1 = UUID.fromString("449cefa5-47cd-4777-adbd-5653b051ef5a");
  private static UUID UUID_2 = UUID.fromString("33123be4-1423-483b-80ae-b558d04d6008");
  private static UUID UUID_3 = UUID.fromString("031e376f-c89a-47b6-a380-8fc854d2b805");
  private static UUID UUID_4 = UUID.fromString("850426ff-f01c-425e-b9e9-d0dd77bd8a1f");

  private static ClassOfStudents MR_WILLIAMS_CLASS = new ClassOfStudents(
      UUID_1,
      "Mr Williams' Class",
      UUID_2
  );

  private static ClassOfStudents MR_MACLEOD_CLASS = new ClassOfStudents(
      UUID_3,
      "Mr Macleod's Class",
      UUID_4
  );

  @Test
  public void addingAndUpdatingClassSavesToRepo() {
    classOfStudentsController.addClassOfStudents(MR_WILLIAMS_CLASS);
    classOfStudentsController.updateClassOfStudents(MR_MACLEOD_CLASS);
    
    verify(classOfStudentsRepository).save(MR_WILLIAMS_CLASS);
    verify(classOfStudentsRepository).save(MR_MACLEOD_CLASS);
  }

  @Test
  public void getsClassWithTheCorrectUUID() {
    when(classOfStudentsRepository.findById(UUID_1)).thenReturn(Optional.of(MR_WILLIAMS_CLASS));
    when(classOfStudentsRepository.findById(UUID_3)).thenReturn(Optional.of(MR_MACLEOD_CLASS));

    ClassOfStudents payload1 = classOfStudentsController.getClassOfStudents(UUID_1);
    ClassOfStudents payload2 = classOfStudentsController.getClassOfStudents(UUID_3);

    verify(classOfStudentsRepository).findById(UUID_1);
    verify(classOfStudentsRepository).findById(UUID_3);

    assertThat(payload1).isNotNull();
    assertThat(payload2).isNotNull();

    assertThat(payload1.getClassId()).isEqualTo(UUID_1);
    assertThat(payload1.getTeacherId()).isEqualTo(UUID_2);
    assertThat(payload2.getClassId()).isEqualTo(UUID_3);
    assertThat(payload2.getTeacherId()).isEqualTo(UUID_4);
  }

  @Test
  public void deletesClassWithTheCorrectUUID() {
    classOfStudentsController.deleteClassOfStudents(UUID_1);
    classOfStudentsController.deleteClassOfStudents(UUID_3);

    verify(classOfStudentsRepository).deleteById(UUID_1);
    verify(classOfStudentsRepository).deleteById(UUID_3);
  }
}