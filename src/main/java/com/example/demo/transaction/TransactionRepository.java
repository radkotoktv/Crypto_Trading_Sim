package com.example.demo.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TransactionDTO save(TransactionDTO transaction) {
        String sql = "INSERT INTO transactions (user_id, crypto_id, type, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                transaction.getUser_id(),
                transaction.getCrypto_id(),
                transaction.getType(),
                transaction.getQuantity(),
                transaction.getUnit_price());
        return transaction;
    }

    public List<Transaction> findByUserId(Long userId) {
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new TransactionRowMapper(), userId);
    }
}