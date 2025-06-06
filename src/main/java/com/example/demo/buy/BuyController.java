package com.example.demo.buy;

import com.example.demo.account.balance.BalanceService;
import com.example.demo.holding.HoldingService;
import com.example.demo.transaction.TransactionDTO;
import com.example.demo.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BuyController {
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private HoldingService holdingService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(@RequestBody TransactionDTO transaction) {
        try {
            if (transaction.getUser_id() == null || transaction.getCrypto_id() == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Missing user_id or crypto_id")
                );
            }
            double currentBalance = balanceService.getBalance(transaction.getUser_id());
            double totalCost = transaction.getUnit_price() * transaction.getQuantity();
            if (currentBalance < totalCost) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Insufficient funds",
                                "currentBalance", currentBalance,
                                "required", totalCost));
            }

            double newBalance = currentBalance - totalCost;
            updateBalance(transaction, currentBalance, totalCost);
            updateHolding(transaction);
            TransactionDTO createdTransaction = createTransactionInController(transaction, totalCost);
            return ResponseEntity.ok(Map.of("status", "success",
                                            "transaction", createdTransaction,
                                            "newBalance", newBalance));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Transaction failed",
                            "message", e.getMessage())
            );
        }
    }

    private void updateBalance(TransactionDTO transaction, double currentBalance, double totalCost) {
        balanceService.saveBalance(transaction.getUser_id(), currentBalance - totalCost);
    }

    private void updateHolding(TransactionDTO transaction) {
        holdingService.saveHolding(transaction.getUser_id(),
                                    transaction.getCrypto_id(),
                                    transaction.getQuantity());
    }

    private TransactionDTO createTransactionInController(TransactionDTO transaction, double totalCost) {
        return transactionService.createTransaction(
                new TransactionDTO(transaction.getUser_id(),
                        transaction.getCrypto_id(),
                        transaction.getType(),
                        transaction.getQuantity(),
                        transaction.getUnit_price(),
                        totalCost));
    }
}