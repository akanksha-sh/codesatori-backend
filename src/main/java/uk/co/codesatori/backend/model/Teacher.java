package uk.co.codesatori.backend.model;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class Teacher extends User {

  public Teacher(UUID id, String school) {
    super(id, "ROLE_TEACHER", school);
  }

  public Teacher() {}

}
