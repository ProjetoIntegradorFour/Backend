// src/main/java/com/libapp/backend/dto/UserUpdateRequestDTO.java
package com.libapp.backend.dto;

import java.util.Set;

public class UserUpdateRequestDTO {
    private String name;
    private String email;
    private Boolean active;
    private Set<String> roleNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Set<String> roleNames) {
        this.roleNames = roleNames;
    }
}