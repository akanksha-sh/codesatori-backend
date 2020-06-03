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

  public ClassOfStudents() {
  }

  public ClassOfStudents(UUID classId, String name, UUID teacherId) {
    this.classId = classId;
    this.name = name;
    this.teacherId = teacherId;
  }

  @Id
  private UUID classId;
  private String name;
  private UUID teacherId;
}
