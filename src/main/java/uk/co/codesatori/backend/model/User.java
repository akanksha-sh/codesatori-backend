package uk.co.codesatori.backend.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public abstract class User {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;
  private String username;
  private String password;

  public User(UUID id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public User() {}
}