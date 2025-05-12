package com.example.demo.account.profile;

import java.time.LocalDateTime;

public class ProfileCreateRequestDTO {
    private String username;
    private String password_hash;
    private String email;
    private LocalDateTime created_at;

    public ProfileCreateRequestDTO() {

    }

    public ProfileCreateRequestDTO(String username, String password_hash, String email, LocalDateTime created_at) {
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
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
