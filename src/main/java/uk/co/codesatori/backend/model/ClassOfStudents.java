package uk.co.codesatori.backend.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "classes_of_students")
@Getter
@Setter
public class ClassOfStudents {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(updatable = false, nullable = false)
  private UUID classId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private UUID teacherId;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "class_to_student_mapping", joinColumns = @JoinColumn(name = "class_id"))
  @Column(name = "student_id")
  private Set<UUID> studentIds = new HashSet<>();

  @OneToMany
  @JoinColumn(name = "class_id")
  private Set<AssignmentStatus> assignmentStatus = new HashSet<>();

  public ClassOfStudents(String name, UUID teacherId, Set<UUID> studentsIds,
      Set<AssignmentStatus> assignmentStatus) {
    this.name = name;
    this.teacherId = teacherId;
    this.studentIds = studentsIds;
    this.assignmentStatus = assignmentStatus;
  }

  public ClassOfStudents() {
  }

  public boolean containsStudentWithId(UUID studentId) {
    return studentIds.contains(studentId);
  }

  public boolean isTaughtByTeacherWithId(UUID teacherId) {
    return this.teacherId.equals(teacherId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof ClassOfStudents) {
      ClassOfStudents that = (ClassOfStudents) obj;
      return Objects.equals(this.classId, that.classId);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId);
  }

//  @Getter
//  @Setter
//  @Embeddable
//  public static class AssignmentDeadline {
//
//    @NotNull
//    private UUID assignmentId;
//
//    @NotNull
//    private Timestamp deadline;
//
//    public AssignmentDeadline(@NotNull UUID assignmentId,
//                              @NotNull Timestamp deadline) {
//      this.assignmentId = assignmentId;
//      this.deadline = deadline;
//    }
//
//    public AssignmentDeadline() {
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//      if (this == obj) {
//        return true;
//      } else if (obj instanceof AssignmentDeadline) {
//        AssignmentDeadline that = (AssignmentDeadline) obj;
//        return Objects.equals(this.assignmentId, that.assignmentId);
//      }
//      return false;
//    }
//
//    @Override
//    public int hashCode() {
//      return Objects.hash(assignmentId);
//    }
//  }
}