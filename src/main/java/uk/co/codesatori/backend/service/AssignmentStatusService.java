package uk.co.codesatori.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.codesatori.backend.model.Assignment;
import uk.co.codesatori.backend.model.AssignmentStatus;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.StudentSubmission;
import uk.co.codesatori.backend.repositories.AssignmentRepository;
import uk.co.codesatori.backend.repositories.AssignmentStatusRepository;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.repositories.StudentSubmissionRepository;

import java.util.*;

@Service
public class AssignmentStatusService {

  @Autowired
  private ClassOfStudentsRepository classOfStudentsRepository;

  @Autowired
  private StudentSubmissionRepository studentSubmissionRepository;

  @Autowired
  private AssignmentStatusRepository assignmentStatusRepository;

  @Autowired
  private AssignmentRepository assignmentRepository;

  public Optional<AssignmentStatus> publish(AssignmentStatus assignmentStatus) {
    UUID classId = assignmentStatus.getClassId();
    UUID assignmentId = assignmentStatus.getAssignmentId();
    Set<StudentSubmission> studentSubmissions = new HashSet<>();
    Map<String, Object> assignmentTemplate;
    Set<UUID> students;

    Optional<ClassOfStudents> classOfStudents = classOfStudentsRepository.findById(classId);
    if (classOfStudents.isEmpty()) {
      return Optional.empty();
    } else {
      students = classOfStudents.get().getStudentIds();
    }

    Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
    if (assignment.isEmpty()) {
      return Optional.empty();
    } else {
      assignmentTemplate = assignment.get().getAssignmentTemplate();
    }

//    for (UUID student : students) {
//      StudentSubmission submission
//          = new StudentSubmission(
//              classId,
//              assignmentId,
//              student,
//              assignment.get().getName(),
//              null,
//              -1,
//              -1,
//              assignmentTemplate);
//      studentSubmissionRepository.save(submission);
//      studentSubmissions.add(submission);
//    }
//
//    assignmentStatus.setStudentSubmissions(studentSubmissions);
    assignmentStatusRepository.save(assignmentStatus);
    return Optional.of(assignmentStatus);
  }

  public Optional<StudentSubmission> getStudentSubmission(UUID classId, UUID assignmentId, UUID studentId) {
    return studentSubmissionRepository.findById(
            new StudentSubmission.StudentSubmissionId(classId, assignmentId, studentId));
  }

  public Map<Assignment, StudentSubmission> getStudentSubmissions(UUID studentId) {
    Map<Assignment, StudentSubmission> studentSubmissions = new HashMap<>();
    studentSubmissionRepository.findAll().forEach(studentSubmission -> {
      if (studentSubmission.getStudentId().equals(studentId)) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(studentSubmission.getAssignmentId());
        assignmentOptional.ifPresent(assignment -> studentSubmissions.put(assignment, studentSubmission));
      }
    });
    return studentSubmissions;
  }
}
