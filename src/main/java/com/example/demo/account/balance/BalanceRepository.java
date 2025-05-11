package com.example.demo.account.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BalanceRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Optional<Balance> findById(Long user_id) {
        try {
            String sql = "SELECT * FROM account_balance WHERE user_id = ?";
            Balance balance = jdbcTemplate.queryForObject(sql, new Object[]{user_id}, (rs, rowNum) -> {
                Balance b = new Balance();
                b.setUser_id(rs.getLong("user_id"));
                b.setBalance(rs.getDouble("balance"));
                return b;
            });
            return Optional.ofNullable(balance);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean save(Balance balance) {
        String select = "SELECT user_id FROM account_balance";
        boolean exists = jdbcTemplate.queryForList(select, Long.class).stream()
                .anyMatch(p -> p.equals(balance.getUser_id()));

        if (!exists) {
            String sql = "INSERT INTO account_balance (user_id, balance) VALUES (?, ?)";
            jdbcTemplate.update(sql, balance.getUser_id(), balance.getBalance());
        } else {
            String sql = "UPDATE account_balance SET balance = ? WHERE user_id = ?";
            jdbcTemplate.update(sql, balance.getBalance(), balance.getUser_id());
        }
        return !exists;
    }
}
