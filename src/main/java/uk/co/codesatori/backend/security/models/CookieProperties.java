package uk.co.codesatori.backend.security.models;

import lombok.Data;

@Data
public class CookieProperties {

  String domain;
  String path;
  boolean httpOnly;
  boolean secure;
  int maxAgeInMinutes;
}
