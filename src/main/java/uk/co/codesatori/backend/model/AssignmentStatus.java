package uk.co.codesatori.backend.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IdClass(AssignmentStatus.AssignmentStatusId.class)
@Table(name = "assignment_status")
@Entity
public class AssignmentStatus {

  @Id
  @Column(name = "class_id", updatable = false, nullable = false)
  private UUID classId;

  @Id
  @Column(name = "assignment_id", updatable = false, nullable = false)
  private UUID assignmentId;

  @Column(nullable = false)
  private Timestamp deadline;

  @Column(nullable = false)
  private int status;

//  // TODO: Remove this because this will make requests extremely large
//  @OneToMany
//  @JoinColumns({
//    @JoinColumn(name = "class_id"),
//    @JoinColumn(name = "assignment_id")
//  })
//  private Set<StudentSubmission> studentSubmissions;

  public AssignmentStatus(UUID classId, UUID assignmentId, Timestamp deadline,
                          int status) {
    this.classId = classId;
    this.assignmentId = assignmentId;
    this.deadline = deadline;
    this.status = status;
//    this.studentSubmissions = Collections.emptySet();
  }

  public AssignmentStatus(UUID classId, UUID assignmentId, Timestamp deadline,
                          int status, Set<StudentSubmission> studentSubmissions) {
    this.classId = classId;
    this.assignmentId = assignmentId;
    this.deadline = deadline;
    this.status = status;
//    this.studentSubmissions = studentSubmissions;
  }

  public AssignmentStatus() {
  }

//  public boolean hasStudent(UUID studentId) {
//    for (StudentSubmission submission : studentSubmissions) {
//      if (submission.getStudentId().equals(studentId)) {
//        return true;
//      }
//    }
//    return false;
//  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof AssignmentStatus) {
      AssignmentStatus that = (AssignmentStatus) obj;
      return Objects.equals(this.assignmentId, that.assignmentId) && Objects
          .equals(this.classId, that.classId);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, assignmentId);
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class AssignmentStatusId implements Serializable {

    private UUID classId;
    private UUID assignmentId;
  }

  public enum Status {
    PENDING,
    MARKED;

    public static Status of(int value) {
      List<Status> statuses = Arrays.asList(Status.values());
      for (Status status : statuses) {
        if (status.value() == value) {
          return status;
        }
      }
      throw new UnsupportedOperationException("There is no STATUS corresponding to " + value + ".");
    }

    public int value() {
      switch (this) {
        case PENDING:
          return 0;
        case MARKED:
          return 1;
        default:
          throw new UnsupportedOperationException("This STATUS has no value.");
      }
    }
  }
}