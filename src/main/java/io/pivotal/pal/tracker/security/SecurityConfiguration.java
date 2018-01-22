package io.pivotal.pal.tracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Value("${SECURITY_FORCE_HTTPS}") private Boolean SECURITY_FORCE_HTTPS;

  @Autowired public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
  }

  @Override protected void configure(HttpSecurity httpSecurity) throws Exception {
    if (SECURITY_FORCE_HTTPS) {
      httpSecurity.requiresChannel().anyRequest().requiresSecure();
    }

    httpSecurity.authorizeRequests()
        .antMatchers("/**")
        .hasRole("USER")
        .and()
        .httpBasic()
        .and()
        .csrf()
        .disable();
  }
}
