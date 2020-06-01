package uk.co.codesatori.backend.services;

import static java.util.Collections.emptyList;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthenticationService {

  private static final long EXPIRATION_TIME = 365 * 24 * 60 * 60;
  private static final String SIGNING_KEY = "codeSatoriSigningKey";
  private static final String BEARER_PREFIX = "Bearer";
  private static final String ACCESS_CONTROL_EXPOSE_HEADER = "Access-Control-Expose-Headers";
  private static final String AUTH_HEADER = "Authorisation";

  // Adds the JWT token after successful authentication, containing the username,
  // to authorization header field.
  static public void addAuthentication(HttpServletResponse response, String username) {
    String jwtToken = Jwts.builder().setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
        .compact();
    response.addHeader(AUTH_HEADER, BEARER_PREFIX + " " + jwtToken);
    response.addHeader(ACCESS_CONTROL_EXPOSE_HEADER, AUTH_HEADER);
  }

  // Tries to get the username from the JWT in the authorization header, and then
  // returns a valid authentication token for that user if found.
  static public Authentication getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(AUTH_HEADER);
    if (token != null) {
      String user = Jwts.parser()
          .setSigningKey(SIGNING_KEY)
          .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
          .getBody()
          .getSubject();

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, emptyList());
      } else {
        throw new RuntimeException("Authentication failed");
      }
    }
    return null;
  }
}