package com.example.demo.account.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BalanceController {
    @Autowired
    BalanceService balanceService;

     @PostMapping("/balance")
     public void saveBalance(@RequestBody Balance balance) {
         balanceService.saveBalance(balance.getUser_id(), balance.getBalance());
     }

     @GetMapping("/balance/{user_id}")
     public Double getBalance(@PathVariable Long user_id) {
         return balanceService.getBalance(user_id);
     }

}
