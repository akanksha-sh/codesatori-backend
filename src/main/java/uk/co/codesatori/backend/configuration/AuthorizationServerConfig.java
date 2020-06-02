package uk.co.codesatori.backend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  static final String CLIENT_ID = "codesatori-web";
  static final String CLIENT_SECRET = "$2y$12$HpfJW6LvKn2Ft3OIMmwBuenWLMhFD.IOpoHhacegJKRiF5Ov5..U2"; //""
  static final String GRANT_TYPE_PASSWORD = "password";
  static final String AUTHORIZATION_CODE = "authorization_code";
  static final String REFRESH_TOKEN = "refresh_token";
  static final String IMPLICIT = "implicit";
  static final String SCOPE_READ = "read";
  static final String SCOPE_WRITE = "write";
  static final String TRUST = "trust";
  static final String SIGNING_KEY = "as466gf";
  static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1*60*60;
  static final int REFRESH_TOKEN_VALIDITY_SECONDS = 7*24*60*60;

  //This is autowired using the AuthenticationManagerBean we set up previously in SecurityConfig
  @Autowired
  private AuthenticationManager authenticationManager;

  //Set the signing_key for our JWT access tokens, so we know the access tokens received are signed by us,
  //Translator between a JWT-encoded token and OAuth authentication information,
  //used by the JWTTokenStore to validate the access tokens.
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(SIGNING_KEY);
    return converter;
  }

  //A TokenStore implementation that just reads data from the tokens themselves.
  // Not really a store since it never persists anything, and methods like
  // getAccessToken(OAuth2Authentication) always return null. But nevertheless a
  // useful tool since it translates access tokens to and from authentications.
  // Use this wherever a TokenStore is needed, but remember to use the same
  // JwtAccessTokenConverter instance (or one with the same verifier) as was
  // used when the tokens were minted.
  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  // Configures the client details service.
  // Holds all session info in memory so it does not persist, but this doesn't matter since we are using JWT
  // Checks the client ID of the request and sets the scopes and grant types allowed
  // as well as the duration of validity of each token
  @Override
  public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
    configurer
        .inMemory()
        .withClient(CLIENT_ID)
        .secret(CLIENT_SECRET)
        .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT )
        .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
        .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS).
        refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
  }

  // Configures the token store to use our JWT token store, and our authentication manager
  // for the password grant type.
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.tokenStore(tokenStore())
        .authenticationManager(authenticationManager)
        .accessTokenConverter(accessTokenConverter());
  }
}