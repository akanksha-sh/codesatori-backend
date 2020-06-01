package uk.co.codesatori.backend.services;

import uk.co.codesatori.backend.domain.User;
import uk.co.codesatori.backend.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service(value = "userService")
public class UserDetailsServiceImpl implements UserDetailsService {

  private UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // Finds a User by the userName, and returns a Spring User object if found.
  // If not, throws a UsernameNotFoundException
  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(userName)
        .orElseThrow(() -> new UsernameNotFoundException("User: " + userName + " not found"));
    return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
        Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
  }
}