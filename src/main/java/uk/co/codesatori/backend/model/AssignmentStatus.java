package uk.co.codesatori.backend.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
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

  public AssignmentStatus(UUID classId, UUID assignmentId, Timestamp deadline, int status) {
    this.classId = classId;
    this.assignmentId = assignmentId;
    this.deadline = deadline;
    this.status = status;
  }

  public AssignmentStatus() {
  }

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