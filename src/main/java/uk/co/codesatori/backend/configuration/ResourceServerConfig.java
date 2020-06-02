package uk.co.codesatori.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  private static final String RESOURCE_ID = "resource_id";


  // Indicate that not only token-based authentication is allowed on these resources.
  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(RESOURCE_ID).stateless(false);
  }

  //Configure security of our resource servers,
  //only authorisations with role of ADMIN can access /classes* endpoint
  //and exception handling is handled by the OAuth2AccessDeniedHandler
  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.
        anonymous().disable()
        .authorizeRequests()
        .antMatchers("/classes*").access("hasRole('ADMIN')")
        .and()
        .exceptionHandling()
        .accessDeniedHandler(new OAuth2AccessDeniedHandler());
  }
}