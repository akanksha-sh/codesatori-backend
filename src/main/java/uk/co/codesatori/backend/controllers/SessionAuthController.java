package uk.co.codesatori.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SessionCookieOptions;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.security.SecurityService;
import uk.co.codesatori.backend.security.models.Credentials;
import uk.co.codesatori.backend.security.models.SecurityProperties;
import uk.co.codesatori.backend.utils.CookieUtils;

@RestController
public class SessionAuthController {

  @Autowired
  SecurityService securityService;

  @Autowired
  CookieUtils cookieUtils;

  @Autowired
  SecurityProperties secProps;

  @PostMapping("/session/login")
  public void sessionLogin(HttpServletRequest request) {
    String idToken = securityService.getBearerToken(request);
    int sessionExpiryDays = secProps.getFirebaseProps().getSessionExpiryInDays();
    long expiresIn = TimeUnit.DAYS.toMillis(sessionExpiryDays);
    SessionCookieOptions options = SessionCookieOptions.builder().setExpiresIn(expiresIn).build();
    try {
      String sessionCookieValue = FirebaseAuth.getInstance().createSessionCookie(idToken, options);
      cookieUtils.setSecureCookie("session", sessionCookieValue,
          (int) TimeUnit.DAYS.toMinutes(sessionExpiryDays));
      cookieUtils.setCookie("authenticated", Boolean.toString(true),
          (int) TimeUnit.DAYS.toMinutes(sessionExpiryDays));
    } catch (FirebaseAuthException e) {
      e.printStackTrace();
    }
  }

  @PostMapping("/session/logout")
  public void sessionLogout() {
    if (securityService.getCredentials().getType() == Credentials.CredentialType.SESSION
        && secProps.getFirebaseProps().isEnableLogoutEverywhere()) {
      try {
        FirebaseAuth.getInstance().revokeRefreshTokens(securityService.getUser().getUid());
      } catch (FirebaseAuthException e) {
        e.printStackTrace();
      }
    }
    cookieUtils.deleteSecureCookie("session");
    cookieUtils.deleteCookie("authenticated");

  }

}
