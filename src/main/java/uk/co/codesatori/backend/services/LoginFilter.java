package uk.co.codesatori.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

  public LoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  // Does the actual authentication, returns a populated Authentication object
  // on success, if not returns null
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
      HttpServletResponse res)
      throws AuthenticationException, IOException {
    LoginCredentials loginCredentials = new ObjectMapper()
        .readValue(req.getInputStream(), LoginCredentials.class);
    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(
            loginCredentials.getUsername(),
            loginCredentials.getPassword(),
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
    AuthenticationService.addAuthentication(res, auth.getName());
  }
}