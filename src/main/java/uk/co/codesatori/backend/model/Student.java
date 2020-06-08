package uk.co.codesatori.backend.model;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class Student extends User {

  public Student(UUID id, String school) {
    super(id, "ROLE_STUDENT", school);
  }

  public Student() {}
}
