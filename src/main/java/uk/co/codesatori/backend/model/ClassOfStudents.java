package uk.co.codesatori.backend.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "classes_of_students")
@Getter
@Setter
public class ClassOfStudents {

  @Id
  private UUID classId;
  private String name;
  private UUID teacherId;

  @ElementCollection
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
}
