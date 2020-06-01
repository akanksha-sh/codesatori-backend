package uk.co.codesatori.backend.model;

import java.util.UUID;

public class Student extends User {

  public Student(UUID id, String username, String password) {
    super(id, username, password);
  }
}
