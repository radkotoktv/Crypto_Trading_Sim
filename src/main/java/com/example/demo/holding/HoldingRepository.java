package com.example.demo.holding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class HoldingRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Optional<Holding>> findById(Long user_id) {
        try {
            String sql = "SELECT * FROM holdings WHERE user_id = ?";
            List<Holding> holdings = jdbcTemplate.query(sql, new Object[]{user_id}, (rs, rowNum) -> {
                Holding h = new Holding();
                h.setUser_id(rs.getLong("user_id"));
                h.setCrypto_id(rs.getLong("crypto_id"));
                h.setQuantity(rs.getInt("quantity"));
                h.setLast_updated(rs.getTimestamp("last_updated").toLocalDateTime());
                return h;
            });

            return holdings.stream()
                    .map(Optional::ofNullable)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public boolean save(HoldingDTO holding, double quantity) {
        String sql = "SELECT COUNT(*) FROM holdings WHERE user_id = ? AND crypto_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, holding.getUser_id(), holding.getCrypto_id());
        if (count != null && count > 0) {
            String getAmount = "SELECT quantity FROM holdings WHERE user_id = ? AND crypto_id = ?";
            int currentAmount = jdbcTemplate.queryForObject(getAmount,
                                                            Integer.class,
                                                            holding.getUser_id(),
                                                            holding.getCrypto_id());

            String update = "UPDATE holdings SET last_updated = ?, quantity = ? WHERE user_id = ? AND crypto_id = ?";
            jdbcTemplate.update(update,
                          LocalDateTime.now(),
                            currentAmount + quantity,
                            holding.getUser_id(),
                            holding.getCrypto_id());
        } else {
            String insert = "INSERT INTO holdings (user_id, crypto_id, quantity, last_updated) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insert,
                                holding.getUser_id(),
                                holding.getCrypto_id(),
                                quantity,
                                LocalDateTime.now());
        }
        return count == null || count == 0;
    }

    public void deleteById(Long user_id, Long crypto_id) {
        String sql = "DELETE FROM holdings WHERE user_id = ? and crypto_id = ?";
        jdbcTemplate.update(sql, user_id, crypto_id);
    }

}
