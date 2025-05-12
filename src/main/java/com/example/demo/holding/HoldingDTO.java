package com.example.demo.holding;

public class HoldingDTO {
    private Long user_id;
    private Long crypto_id;
    private double quantity;

    public HoldingDTO() {

    }

    public HoldingDTO(Long user_id, Long crypto_id, double quantity) {
        this.user_id = user_id;
        this.crypto_id = crypto_id;
        this.quantity = quantity;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
