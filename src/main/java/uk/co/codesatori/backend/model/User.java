package uk.co.codesatori.backend.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class User {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  protected User(UUID id, String firstName, String lastName, String email, String password) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  public User() {
  }
}