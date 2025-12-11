package com.libapp.backend.dto;

import java.util.List;

public class ProfileResponse {
    private Long id;
    private String name;
    private String cpf;
    private List<String> role;

    public ProfileResponse(Long long1, String string, String string2, List<String> roles) {
    }

    public ProfileResponse(String cpf, Long id, String name, List<String> role) {
        this.cpf = cpf;
        this.id = id;
        this.name = name;
        this.role = role;
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

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

}