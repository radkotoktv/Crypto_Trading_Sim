package com.example.demo.transaction;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("id"));
        transaction.setUser_id(rs.getLong("user_id"));
        transaction.setCrypto_id(rs.getLong("crypto_id"));
        transaction.setType(rs.getString("type"));
        transaction.setQuantity(rs.getBigDecimal("quantity"));
        transaction.setUnit_price(rs.getBigDecimal("unit_price"));
        transaction.setTotal_cost(rs.getBigDecimal("total_cost"));
        transaction.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
        return transaction;
    }
}