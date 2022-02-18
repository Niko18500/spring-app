package com.config;

import com.repository.UserRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity @Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public final UserRepository userRepo;

    public SecurityConfig() {
        this.userRepo = UserRepository.get();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User: %s not found", username))
            ));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationManager(authenticationManager());

        http.csrf().disable();

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/register", "/login").permitAll()
                .anyRequest().authenticated();
    }
}
