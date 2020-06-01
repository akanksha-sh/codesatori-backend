package uk.co.codesatori.backend.model;

import java.util.UUID;

public class Teacher extends User {

  public Teacher(UUID id, String username, String password) {
    super(id, username, password);
  }
}
