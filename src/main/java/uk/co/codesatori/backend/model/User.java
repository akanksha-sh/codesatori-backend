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
  private final UUID id;
  private final String username;
  private final String password;

  public User(UUID id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}