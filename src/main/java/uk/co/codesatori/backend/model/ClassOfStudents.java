package uk.co.codesatori.backend.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ClassOfStudents {

  @Id
  private final UUID classId;
  private final String name;
  private final UUID teacherId;
  private final Collection<UUID> studentIDs;

  public ClassOfStudents(UUID classId, String name, UUID teacherId) {
    this.classId = classId;
    this.name = name;
    this.teacherId = teacherId;
    this.studentIDs = new LinkedList<>();
  }

  public UUID getClassId() {
    return classId;
  }

  public String getName() {
    return name;
  }

  public UUID getTeacherId() {
    return teacherId;
  }

  public Collection<UUID> getStudentIDs() {
    return studentIDs;
  }
}
