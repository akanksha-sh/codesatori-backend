package uk.co.codesatori.backend.model;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class Teacher extends User {

  public Teacher(UUID id, String firstName, String lastName, String email, String password) {
    super(id, firstName, lastName, email, password);
  }
}
