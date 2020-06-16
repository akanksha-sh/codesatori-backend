package uk.co.codesatori.backend.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.Compiler;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class UserController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable String id) {
    /* TODO: this needs phasing out and updating to use a similar system as below. */
    return userRepository.findById(User.FirebaseUUID.toUUID(id)).orElse(null);
  }

  // Only allows the user to edit his own details
  @PostMapping("/user")
  public void signUpNewUser(@RequestBody User user) {
    UUID id = securityService.getCurrentUUID();
    if (userRepository.findById(id).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists in database.");
    }
    user.setId(id);
    // TODO: check if the fields of user are input correctly
    userRepository.save(user);
  }
}
