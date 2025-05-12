package com.example.demo.holding;

import java.time.LocalDateTime;

public class Holding {
    private Long user_id;
    private Long crypto_id;
    private int quantity;
    private LocalDateTime last_updated;

    public Holding() {

    }

    public Holding(Long user_id, Long crypto_id, int quantity, LocalDateTime last_updated) {
        this.user_id = user_id;
        this.crypto_id = crypto_id;
        this.quantity = quantity;
        this.last_updated = last_updated;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getCrypto_id() {
        return crypto_id;
    }

    public void setCrypto_id(Long crypto_id) {
        this.crypto_id = crypto_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(LocalDateTime last_updated) {
        this.last_updated = last_updated;
    }
}
