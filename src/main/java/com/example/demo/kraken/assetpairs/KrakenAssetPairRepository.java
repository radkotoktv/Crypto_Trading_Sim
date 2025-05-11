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



}
