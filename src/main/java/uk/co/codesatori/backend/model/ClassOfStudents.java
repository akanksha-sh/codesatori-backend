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

  @ElementCollection
  @CollectionTable(name = "class_to_assignment_mapping", joinColumns = @JoinColumn(name = "class_id"))
  @Column(name = "assignment_id")
  private Set<UUID> assignmentIds = new HashSet<>();

  public ClassOfStudents(UUID classId, String name, UUID teacherId, Set<UUID> studentsIds) {
    this.classId = classId;
    this.name = name;
    this.teacherId = teacherId;
    this.studentIds = studentsIds;
  }

  public ClassOfStudents() {
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof ClassOfStudents) {
      ClassOfStudents that = (ClassOfStudents) obj;
      return Objects.equals(this.classId, that.classId) &&
          Objects.equals(this.name, that.name) &&
          Objects.equals(this.teacherId, that.teacherId) &&
          Objects.equals(this.studentIds, that.studentIds) &&
          Objects.equals(this.assignmentIds, that.assignmentIds);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, name, teacherId, studentIds, assignmentIds);
  }
}
