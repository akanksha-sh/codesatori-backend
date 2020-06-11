package uk.co.codesatori.backend.security.model;

import lombok.Data;

@Data
public class CookieProperties {

  String domain;
  String path;
  boolean httpOnly;
  boolean secure;
  int maxAgeInMinutes;
}
