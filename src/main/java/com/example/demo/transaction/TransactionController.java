package com.example.demo.transaction;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        return transactionService.getUserTransactions(userId);
    }

    @GetMapping("/user/{userId}/filter")
    public List<Transaction> getUserTransactionsWithFilters(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cryptoId,
            @RequestParam(required = false) String type) {
        return transactionService.getUserTransactionsWithFilters(userId, cryptoId, type);
    }
}