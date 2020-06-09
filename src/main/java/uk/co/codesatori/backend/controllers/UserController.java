package uk.co.codesatori.backend.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class UserController {

  private final UserRepository userRepository;

  @Autowired
  SecurityService securityService;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private UUID toUUID(String uid) {
    return UUID.nameUUIDFromBytes(uid.getBytes());
  }

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable String id) {
    return userRepository.findById(toUUID(id)).orElse(null);
  }

  @GetMapping("/user")
  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  @PostMapping("/user")
  public void addUserFromFrontend(@RequestBody User user) {
    String uid = securityService.getUser().getUid();
    if (getUser(uid) != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists in database");
    }
    user.setId(toUUID(uid));
    // TODO: check if the fields of user are input correctly
    addUser(user);
  }

  public void addUser(User user) {
    userRepository.save(user);
  }

  @PutMapping("/user")
  public void updateUser(@RequestBody User user) {
    userRepository.save(user);
  }

  @DeleteMapping("/user/{id}")
  public void deleteUser(@PathVariable String id) {
    userRepository.deleteById(toUUID(id));
  }

}
