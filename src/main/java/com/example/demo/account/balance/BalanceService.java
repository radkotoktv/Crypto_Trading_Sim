package com.example.demo.account.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    @Autowired
    BalanceRepository balanceRepository;

    public void saveBalance(Long user_id, Double balance) {
        Balance balanceObj = new Balance(user_id, balance);
        balanceRepository.save(balanceObj);
    }

    public Double getBalance(Long user_id) {
        return balanceRepository.findById(user_id)
                .map(Balance::getBalance)
                .orElse(null);
    }
}
