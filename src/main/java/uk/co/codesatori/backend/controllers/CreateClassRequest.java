package uk.co.codesatori.backend.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.repositories.UserRepository;

import java.util.*;

@Getter
@Setter
public class CreateClassRequest {
  private String name;
  private UUID teacherId;
  private List<String> emails = new ArrayList<>();

  public CreateClassRequest() {}

  public CreateClassRequest(String name, UUID teacherId, List<String> emails) {
    this.name = name;
    this.teacherId = teacherId;
    this.emails = emails;
  }

  public ClassOfStudents getClassOfStudents(UserRepository userRepository) {
    Set<UUID> studentIds = new HashSet<>();
    emails.forEach(email -> {
      userRepository.findUserByEmail(email).ifPresent(user -> {
        studentIds.add(user.getId());
      });
    });
    return new ClassOfStudents(name, teacherId, true, studentIds, Collections.EMPTY_SET);
  }
}
