package uk.co.codesatori.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.codesatori.backend.model.AssignmentStatus;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.StudentSubmission;
import uk.co.codesatori.backend.repositories.AssignmentRepository;
import uk.co.codesatori.backend.repositories.AssignmentStatusRepository;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.repositories.StudentSubmissionRepository;

import java.time.LocalDateTime;
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
    Set<UUID> students;

    assignmentStatusRepository.save(assignmentStatus);

    Optional<ClassOfStudents> classOfStudents = classOfStudentsRepository.findById(classId);
    if (classOfStudents.isEmpty()) {
      return Optional.empty();
    } else {
      students = classOfStudents.get().getStudentIds();
    }

    for (UUID student : students) {
      StudentSubmission submission
          = new StudentSubmission(
              classId,
              assignmentId,
              student,
              null,
              -1,
              -1,
              new HashMap<>());
      studentSubmissionRepository.save(submission);
    }

    return Optional.of(assignmentStatus);
  }

  public void save(StudentSubmission studentSubmission) {
    studentSubmissionRepository.save(studentSubmission);
  }

  public void submit(StudentSubmission studentSubmission) {
    studentSubmission.setSubmissionDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
    studentSubmissionRepository.save(studentSubmission);
  }
}
