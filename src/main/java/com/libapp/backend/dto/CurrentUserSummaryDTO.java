package com.libapp.backend.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.libapp.backend.entity.User;

public class CurrentUserSummaryDTO {
    private Long id;
    private String name;
    private String cpf;
    private Set<String> roles;

    public static CurrentUserSummaryDTO fromUser(User user) {
        CurrentUserSummaryDTO dto = new CurrentUserSummaryDTO();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.cpf = user.getCpf();
        dto.roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}