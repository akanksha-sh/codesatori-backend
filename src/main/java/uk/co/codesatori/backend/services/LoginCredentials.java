package uk.co.codesatori.backend.services;

class LoginCredentials {
  private final String username, password;

  LoginCredentials(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
