package com.blogify.BlogApp.dto;

import java.util.Set;

public class LoginResponse {
    private Long id;
    private Set<String> roles;

    public LoginResponse(Long id, Set<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
