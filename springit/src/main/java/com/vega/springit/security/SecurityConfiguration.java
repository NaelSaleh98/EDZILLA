package com.vega.springit.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//this package for "spring-boot-starter-security"
//the default behavior for "spring-boot-starter-security" is in order to access any page in the website you must be logged in.
//so we need to override default behavior
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

//the default behavior for "spring-boot-starter-security" is in order to access any page in the website you must be logged in.
    @Override //override default behavior
    protected void configure(HttpSecurity http) throws Exception {

        http.
                    authorizeRequests()
                    .requestMatchers(EndpointRequest.to("info")).permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN") //ADMIN == ROLE_ADMIN
                    .antMatchers("/actuator/").hasRole("ADMIN")
                    .antMatchers("/").permitAll()
                    .antMatchers("/link/submit").hasRole("USER")
                    .antMatchers("/h2-console/**").permitAll()

                .and().
                    formLogin().loginPage("/login")
                    .permitAll().usernameParameter("email")
                .and()
                    .logout()
                .and()
                    .rememberMe()
         .and()  //turned off CSRF  for the H2 Console to work
                .csrf().disable()
                .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


}

