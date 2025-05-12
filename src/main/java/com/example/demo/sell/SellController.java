package com.example.demo.sell;

import com.example.demo.account.balance.BalanceService;
import com.example.demo.holding.Holding;
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
public class SellController {
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private HoldingService holdingService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(@RequestBody TransactionDTO transaction) {
        try {
            if (transaction.getUser_id() == null || transaction.getCrypto_id() == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Missing user_id or crypto_id")
                );
            }
            Holding holding = getHoldingById(transaction.getUser_id(),
                                            transaction.getCrypto_id());
            if (holding.getQuantity() < transaction.getQuantity()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Insufficient funds",
                                "currentBalance", holding.getQuantity(),
                                "required", transaction.getQuantity()));
            }

            double newBalance = calculateNewBalance(transaction);
            updateBalance(transaction, newBalance);
            updateHolding(transaction);
            TransactionDTO createdTransaction = createTransactionInController(transaction);
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

    private Holding getHoldingById(Long user_id, Long crypto_id) {
        return holdingService.getHoldingsById(user_id)
                .stream()
                .filter(h -> h.getCrypto_id().equals(crypto_id))
                .findFirst()
                .orElse(null);
    }

    private double calculateNewBalance(TransactionDTO transaction) {
        return (transaction.getUnit_price() * transaction.getQuantity()) +
                balanceService.getBalance(transaction.getUser_id());
    }

    private void updateBalance(TransactionDTO transaction, double newBalance) {
        balanceService.saveBalance(transaction.getUser_id(), newBalance);
    }

    private void updateHolding(TransactionDTO transaction) {
        holdingService.saveHolding(transaction.getUser_id(),
                                    transaction.getCrypto_id(),
                                    -transaction.getQuantity());
    }

    private TransactionDTO createTransactionInController(TransactionDTO transaction) {
        return transactionService.createTransaction(
                new TransactionDTO(transaction.getUser_id(),
                                    transaction.getCrypto_id(),
                                    transaction.getType(),
                                    transaction.getQuantity(),
                                    transaction.getUnit_price(),
                           transaction.getQuantity() * transaction.getUnit_price()));
    }
}
