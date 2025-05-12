package com.example.demo.account.profile;

import java.time.LocalDateTime;

public class ProfileDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime created_at;

    public ProfileDTO() {

    }

    public ProfileDTO(Long id, String username, String email, LocalDateTime created_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
