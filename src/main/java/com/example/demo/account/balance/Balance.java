package com.example.demo.account.balance;

public class Balance {
    Long user_id;
    Double balance;

    public Balance() {

    }

    public Balance(Long user_id, Double balance) {
        this.user_id = user_id;
        this.balance = balance;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long id) {
        this.user_id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
