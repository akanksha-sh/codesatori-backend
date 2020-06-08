package uk.co.codesatori.backend.model;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class Student extends User {

  public Student(UUID id, String firstName, String lastName, String email, String password) {
    super(id, firstName, lastName, email, password);
  }
}
