package uk.co.codesatori.backend.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;

@Service(value = "userDetailsService")
public class UserDetailsRetrievalService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsRetrievalService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Queries the injected database of users by username and wraps the result in special Spring
   * Security object.
   *
   * @param username to be queried.
   * @return the details of the authenticated user.
   * @throws UsernameNotFoundException when a username cannot be found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException(String
                .format("Unable to find a user with username \"%s\".", username)
            )
        );
    /* TODO: this doesn't seem right... Why is it "ROLE_ADMIN" and not "user"? */
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
    );
  }
}