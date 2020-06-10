package uk.co.codesatori.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class UserController {

  @Autowired
  SecurityService securityService;


  private final UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable String id) {
    /* TODO: not sure how to secure this. */
    return userRepository.findById(User.FirebaseUUID.toUUID(id)).orElse(null);
  }

  @PostMapping("/user")
  public void addUser(@RequestBody User user) {
    /* TODO: not sure how to secure this. */
    userRepository.save(user);
  }
}
