package com.example.demo.transaction;

public class TransactionDTO {
    private Long user_id;
    private Long crypto_id;
    private String type;
    private double quantity;
    private double unit_price;
    private double total_cost;
    public TransactionDTO() {}
    public TransactionDTO(Long user_id, Long crypto_id, String type, double quantity, double unit_price, double total_cost) {
        this.user_id = user_id;
        this.crypto_id = crypto_id;
        this.type = type;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_cost = total_cost;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }
}
