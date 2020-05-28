package uk.co.codesatori.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uk.co.codesatori.backend.domain.UserCredentials;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

  public LoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  // Does the actual authentication, returns a populated Authentication object
  // on success, if not returns null
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException, IOException {
    UserCredentials userCredentials = new ObjectMapper()
        .readValue(req.getInputStream(), UserCredentials.class);
    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(
            userCredentials.getUserName(),
            userCredentials.getPassword(),
            Collections.emptyList()
        )
    );
  }

  // Default behaviour on successful authentication, adds the JWT token
  // to the authentication header
  @Override
  protected void successfulAuthentication(
      HttpServletRequest req,
      HttpServletResponse res, FilterChain chain,
      Authentication auth) {
    AuthenticationService.addJWTToken(res, auth.getName());
  }
}