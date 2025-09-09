package com.blogify.BlogApp.dto;

import java.util.Set;

public class LoginResponse {
    private Long id;
    private String email;
    private String name; // optional
    private Set<String> roles;

    public LoginResponse(Long id, String email, String name, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }

    // getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}