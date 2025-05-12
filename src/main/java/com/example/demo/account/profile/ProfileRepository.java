package com.example.demo.account.profile;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class ProfileRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean save(Profile profile) {
        String select = "SELECT username FROM users";
        boolean exists = jdbcTemplate.queryForList(select, String.class).stream()
                .anyMatch(p -> p.equals(profile.getUsername()));

        if (!exists) {
            String sql = "INSERT INTO users (username, password_hash, email, created_at) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                                profile.getUsername(),
                                profile.getPassword_hash(),
                                profile.getEmail(),
                                LocalDateTime.now());
        }
        return !exists;
    }

    public Optional<Profile> findById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            Profile profile = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Profile p = new Profile(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at") != null ?
                                rs.getTimestamp("created_at").toLocalDateTime() :
                                null
                );
                p.setId(rs.getLong("id"));
                return p;
            });
            return Optional.ofNullable(profile);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Profile> findByName(String name) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            Profile profile = jdbcTemplate.queryForObject(sql, new Object[]{name}, (rs, rowNum) -> {
                Profile p = new Profile(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at") != null ?
                                rs.getTimestamp("created_at").toLocalDateTime() :
                                null
                );
                p.setId(rs.getLong("id"));
                return p;
            });
            return Optional.ofNullable(profile);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
