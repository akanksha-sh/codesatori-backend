package uk.co.codesatori.backend.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import uk.co.codesatori.backend.utils.JsonToMapConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Getter
@Setter
@IdClass(StudentSubmission.StudentSubmissionId.class)
@Table(name = "student_submission")
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class StudentSubmission {

  @Id
  @Column(name = "class_id", updatable = false, nullable = false)
  private UUID classId;

  @Id
  @Column(name = "assignment_id", updatable = false, nullable = false)
  private UUID assignmentId;

  @Id
  @Column(name = "student_id", updatable = false, nullable = false)
  private UUID studentId;

  @Column
  private String name;

  @Column(updatable = false)
  private Timestamp submissionDate;

  @Column
  private int score;

  @Column
  private int testsPassed;

  @Type(type = "jsonb")
  @Column(nullable = false, columnDefinition = "jsonb")
  @Convert(attributeName = "data", converter = JsonToMapConverter.class)
  private Map<String, Object> studentSubmissionTemplate = new HashMap<>();

  public StudentSubmission(UUID classId, UUID assignmentId, UUID studentId, String name, Timestamp submissionDate,
                           int score, int testsPassed, Map<String, Object> studentSubmissionTemplate) {
    this.classId = classId;
    this.assignmentId = assignmentId;
    this.studentId = studentId;
    this.name = name;
    this.submissionDate = submissionDate;
    this.score = score;
    this.testsPassed = testsPassed;
    this.studentSubmissionTemplate = studentSubmissionTemplate;
  }

  public StudentSubmission() {}

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof StudentSubmission) {
      StudentSubmission that = (StudentSubmission) obj;
      return Objects.equals(this.assignmentId, that.assignmentId) && Objects
          .equals(this.classId, that.classId) && Objects.equals(this.studentId, that.studentId)
          && Objects.equals(this.studentSubmissionTemplate, that.studentSubmissionTemplate);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, assignmentId, studentId, studentSubmissionTemplate);
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class StudentSubmissionId implements Serializable {

    private UUID classId;
    private UUID assignmentId;
    private UUID studentId;

    public StudentSubmissionId(UUID classId, UUID assignmentId, UUID studentId) {
      this.classId = classId;
      this.assignmentId = assignmentId;
      this.studentId = studentId;
    }
  }
}
