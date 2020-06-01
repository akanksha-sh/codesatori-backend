package uk.co.codesatori.backend.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;

@RestController
public class UserController {

  private final UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable UUID id) {
    return userRepository.findById(id).orElse(null);
  }

  @GetMapping("/user")
  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  @PostMapping("/user")
  void addUser(@RequestBody User user) {
    /* TODO: shouldn't this only add if there's no entity with the id? */
    userRepository.save(user);
  }

  @PutMapping("/user")
  void updateUser(@RequestBody User user) {
    userRepository.save(user);
  }

  @DeleteMapping("/user/{id}")
  void deleteUser(@PathVariable UUID id) {
    userRepository.deleteById(id);
  }

}
