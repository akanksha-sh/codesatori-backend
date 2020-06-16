package uk.co.codesatori.backend.controllers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.*;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.*;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class ClassOfStudentsController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private ClassOfStudentsRepository classOfStudentsRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private AssignmentStatusRepository assignmentStatusRepository;

  @Autowired
  private StudentSubmissionRepository studentSubmissionRepository;

  @GetMapping("/classes/student")
  public List<ClassAssignmentInfo> getClassesOfStudentsForStudentDashboard() {
    /* Verify that request has come from a student. */
    UUID studentId = securityService
        .verifyUserRole(Role.STUDENT, "This channel is for students only.");
    /* Filter the database entries based on the given id. */

    List<ClassAssignmentInfo> result = new ArrayList<>();

    List<ClassOfStudents> studentClasses = StreamSupport.stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.containsStudentWithId(studentId))
        .collect(Collectors.toList());

    for (ClassOfStudents classOfStudents : studentClasses) {
      List<AssignmentSubmissionPair> assignmentsSubmissionsForClass  = assignmentStatusRepository
          .findByClassId(classOfStudents.getClassId())
          .stream()
          .map(AssignmentStatus::getAssignmentId)
          .map(id -> assignmentRepository.findById(id).orElseThrow(IllegalStateException::new))
          .map(assignment -> {
            StudentSubmission studentSubmission = studentSubmissionRepository.findById(new StudentSubmission.StudentSubmissionId(classOfStudents.getClassId(), assignment.getAssignmentId(), studentId)).orElse(null);
            if (studentSubmission == null) return null;
            return new AssignmentSubmissionPair(assignment, studentSubmission);
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

      result.add(new ClassAssignmentInfo(classOfStudents, assignmentsSubmissionsForClass));
    }

    return result;
  }

  @GetMapping("/classes/teacher")
  public List<ClassOfStudents> getClassesOfStudentsForTeacherDashboard() {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "This channel is for teachers only.");
    /* Filter the database entries based on the given id. */
    return StreamSupport.stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.isTaughtByTeacherWithId(teacherId))
        .collect(Collectors.toList());
  }

  @PostMapping("/classes")
  public ClassOfStudents createNewClassOfStudents(@RequestBody CreateClassRequest req) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can create classes.");
    /* Add new class to the database and return to user. */

    req.setTeacherId(teacherId);
    ClassOfStudents classOfStudents = req.getClassOfStudents(userRepository);
    classOfStudentsRepository.save(classOfStudents);
    return classOfStudents;
  }

  @PostMapping("/classes/join")
  public ClassOfStudents createNewClassOfStudents(@RequestParam String code) {
    /* Verify that request has come from a teacher. */
    UUID studentId = securityService
        .verifyUserRole(Role.STUDENT, "Only students can join classes.");
    /* Add new class to the database and return to user. */

    ClassOfStudents classOfStudents = classOfStudentsRepository.findByClassCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class code does not exist."));
    classOfStudents.getStudentIds().add(studentId);
    classOfStudentsRepository.save(classOfStudents);
    return classOfStudents;
  }

  @DeleteMapping("/classes/{classIdString}")
  public void deleteClassOfStudents(@PathVariable String classIdString) {
    /* Verify that request has come from a teacher. */
    UUID teacherId = securityService
        .verifyUserRole(Role.TEACHER, "Only teachers can delete classes.");
    /* Delete class from the database and return void. */

    UUID classId = UUID.fromString(classIdString);

    List<UUID> classIdsByTeacher =
        classOfStudentsRepository
            .findByTeacherId(teacherId)
            .stream()
            .map(ClassOfStudents::getClassId)
            .collect(Collectors.toList());

    if (!classIdsByTeacher.contains(classId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher does not own class he wishes to delete.");
    }

    classOfStudentsRepository.deleteById(classId);
  }

  @Getter
  @Setter
  class ClassAssignmentInfo {
    private ClassOfStudents classOfStudents;
    private List<AssignmentSubmissionPair> assignmentSubmissionPair;

    ClassAssignmentInfo(ClassOfStudents classOfStudents, List<AssignmentSubmissionPair> assignmentSubmissionPair) {
      this.classOfStudents = classOfStudents;
      this.assignmentSubmissionPair = assignmentSubmissionPair;
    }

    ClassAssignmentInfo() { }
  }
}