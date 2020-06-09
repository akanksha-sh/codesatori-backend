package uk.co.codesatori.backend.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER_DETAILS")
@Getter
@Setter
public class User {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;
  private String firstName;
  private String lastName;
  private int role;

  public User(UUID id, String firstName, String lastName, int role) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
  }

  public User() {
  }

  public enum ROLE {
    STUDENT,
    TEACHER;

    public int value() {
      switch (this) {
        case STUDENT:
          return 0;
        case TEACHER:
          return 1;
        default:
          throw new UnsupportedOperationException("This ROLE has no value.");
      }
    }
  }
}