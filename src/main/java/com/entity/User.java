package com.entity;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document("users")
public class User implements UserDetails {

    @Id
    public String id;

    public String username;
    public String password;
    public int level;
    private boolean enabled;

    public User() {}

    public User(String id, String username, String password, boolean enabled, int level) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.level = level;
        this.enabled = true;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static User createNew(String username, String password) {
        return new User(UUID.randomUUID().toString(), username, password, true, 0);
    };

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public ArrayList<GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>();
    }
}
