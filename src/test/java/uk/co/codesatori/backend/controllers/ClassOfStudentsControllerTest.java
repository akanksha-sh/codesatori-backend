package uk.co.codesatori.backend.controllers;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

//  private static ClassOfStudents MR_WILLIAMS_CLASS = new ClassOfStudents(
//      UUID_3,
//      "Mr Williams' Class",
//      UUID_1,
//      studentsIds);
//
//  private static ClassOfStudents MR_MACLEOD_CLASS = new ClassOfStudents(
//      UUID_4,
//      "Mr Macleod's Class",
//      UUID_2,
//      studentsIds);
//
//  @Test
//  public void addingAndUpdatingClassSavesThemToRepo() {
//    classOfStudentsController.addClassOfStudents(MR_WILLIAMS_CLASS);
//    classOfStudentsController.updateClassOfStudents(MR_MACLEOD_CLASS);
//
//    verify(classOfStudentsRepository).save(MR_WILLIAMS_CLASS);
//    verify(classOfStudentsRepository).save(MR_MACLEOD_CLASS);
//  }
//
//  @Test
//  public void getsClassWithTheCorrectUUID() {
//    when(classOfStudentsRepository.findById(UUID_1)).thenReturn(Optional.of(MR_WILLIAMS_CLASS));
//    when(classOfStudentsRepository.findById(UUID_3)).thenReturn(Optional.of(MR_MACLEOD_CLASS));
//
//    ClassOfStudents payload1 = classOfStudentsController.getClassOfStudents(UUID_1);
//    ClassOfStudents payload2 = classOfStudentsController.getClassOfStudents(UUID_3);
//
//    verify(classOfStudentsRepository).findById(UUID_1);
//    verify(classOfStudentsRepository).findById(UUID_3);
//
//    assertThat(payload1).isNotNull();
//    assertThat(payload2).isNotNull();
//
//    assertThat(payload1.getClassId()).isEqualTo(UUID_3);
//    assertThat(payload1.getTeacherId()).isEqualTo(UUID_1);
//    assertThat(payload2.getClassId()).isEqualTo(UUID_4);
//    assertThat(payload2.getTeacherId()).isEqualTo(UUID_2);
//  }
//
//  @Test
//  public void deletesClassWithTheCorrectUUID() {
//    classOfStudentsController.deleteClassOfStudents(UUID_1);
//    classOfStudentsController.deleteClassOfStudents(UUID_3);
//
//    verify(classOfStudentsRepository).deleteById(UUID_1);
//    verify(classOfStudentsRepository).deleteById(UUID_3);
//  }
}