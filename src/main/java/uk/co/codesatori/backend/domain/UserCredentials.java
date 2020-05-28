package uk.co.codesatori.backend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentials {
  private String userName;
  private String password;
}