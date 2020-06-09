package uk.co.codesatori.backend.security.models;

import lombok.Data;

@Data
public class FirebaseProperties {

  int sessionExpiryInDays;
  String databaseUrl;
  boolean enableStrictServerSession;
  boolean enableCheckSessionRevoked;
  boolean enableLogoutEverywhere;

}
