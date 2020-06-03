package uk.co.codesatori.backend.model;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class Teacher extends User {

  public Teacher(UUID id, String username, String password) {
    super(id, username, password);
  }

  public Teacher() {}

}
