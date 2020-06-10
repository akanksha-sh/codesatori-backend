package uk.co.codesatori.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class UserController {

  @Autowired
  SecurityService securityService;


  private final UserRepository userRepository;

  @Autowired
  SecurityService securityService;

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
}
