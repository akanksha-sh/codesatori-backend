package uk.co.codesatori.backend.security;

import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.User.FirebaseUUID;
import uk.co.codesatori.backend.model.User.Role;
import uk.co.codesatori.backend.repositories.UserRepository;
import uk.co.codesatori.backend.security.model.Credentials;
import uk.co.codesatori.backend.security.model.SecurityProperties;
import uk.co.codesatori.backend.security.model.User;
import uk.co.codesatori.backend.utils.CookieUtils;

@Service
public class SecurityService {

  @Autowired
  private HttpServletRequest httpServletRequest;

  @Autowired
  private CookieUtils cookieUtils;

  @Autowired
  private SecurityProperties securityProps;

  @Autowired
  private UserRepository userRepository;

  public UUID verifyUserRole(Role role, String errorMessage) {
    /* Get UUID for the user who made this request.
     * Then probe the user details database to check that the user is of the correct role. */
    UUID id = getCurrentUUID();
    Optional<uk.co.codesatori.backend.model.User> user = userRepository.findById(id);

    /* If the user does not exist in the database, or is not a teacher, throw an error. */
    if (user.isEmpty() || !user.get().getRoleAsEnum().equals(role)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }
    return id;
  }

  public UUID getCurrentUUID() {
    User user = getUser();
    return user != null ? FirebaseUUID.toUUID(user.getUid()) : null;
  }

  public User getUser() {
    User userPrincipal = null;
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Object principal = securityContext.getAuthentication().getPrincipal();
    if (principal instanceof User) {
      userPrincipal = ((User) principal);
    }
    return userPrincipal;
  }

  public Credentials getCredentials() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return (Credentials) securityContext.getAuthentication().getCredentials();
  }

  public boolean isPublic() {
    return securityProps.getAllowedPublicApis().contains(httpServletRequest.getRequestURI());
  }

  public String getBearerToken(HttpServletRequest request) {
    String bearerToken = null;
    String authorization = request.getHeader("Authorization");
    if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
      bearerToken = authorization.substring(7, authorization.length());
    }
    return bearerToken;
  }

}
