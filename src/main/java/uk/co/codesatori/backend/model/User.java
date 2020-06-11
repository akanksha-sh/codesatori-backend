package uk.co.codesatori.backend.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_details")
@Getter
@Setter
public class User {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;
  @Column(nullable = false)
  private String firstName;
  @Column(nullable = false)
  private String lastName;
  @Column(nullable = false)
  private int role;

  public User(UUID id, String firstName, String lastName, int role) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
  }

  public User() {
  }

  public Role getRoleAsEnum() {
    return Role.of(role);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof User) {
      User that = (User) obj;
      return this.role == that.role &&
          Objects.equals(this.id, that.id) &&
          Objects.equals(this.firstName, that.firstName) &&
          Objects.equals(this.lastName, that.lastName);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, role);
  }

  public enum Role {
    STUDENT,
    TEACHER;

    public static Role of(int value) {
      List<Role> roles = Arrays.asList(Role.values());
      for (Role role : roles) {
        if (role.value() == value) {
          return role;
        }
      }
      throw new UnsupportedOperationException("There is no role corresponding to " + value + ".");
    }

    public int value() {
      switch (this) {
        case STUDENT:
          return 0;
        case TEACHER:
          return 1;
        default:
          throw new UnsupportedOperationException(
              "There is no value corresponding to " + this + ".");
      }
    }
  }

  public static class FirebaseUUID {

    public static UUID toUUID(String firebaseId) {
      return UUID.nameUUIDFromBytes(firebaseId.getBytes());
    }
  }
}