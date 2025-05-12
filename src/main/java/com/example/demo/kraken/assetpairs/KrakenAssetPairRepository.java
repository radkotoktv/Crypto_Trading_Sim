package com.example.demo.kraken.assetpairs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class KrakenAssetPairRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean save(String currencyName) {
        String select = "SELECT name FROM cryptocurrencies";
        boolean exists = jdbcTemplate.queryForList(select, String.class).stream()
                .anyMatch(p -> p.equals(currencyName));

        if (!exists) {
            String insert = "INSERT INTO cryptocurrencies (name) VALUES (?)";
            jdbcTemplate.update(insert, currencyName);
        }
        return !exists;
    }

    public Long findIdByName(String name) {
        String sql = "SELECT * FROM cryptocurrencies WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{name}, (rs, rowNum) -> {
            return rs.getLong("id");
        });
    }

    public String findNameById(Long id) {
        String sql = "SELECT * FROM cryptocurrencies WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            return rs.getString("name");
        });
    }


}
