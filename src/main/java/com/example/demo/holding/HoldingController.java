package com.example.demo.holding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HoldingController {
    @Autowired
    HoldingService holdingService;

    @PostMapping("/holdings")
    public HoldingDTO saveHolding(@RequestBody HoldingDTO holding) {
        holdingService.saveHolding(holding.getUser_id(), holding.getCrypto_id(), holding.getQuantity());
        return holding;
    }

    @GetMapping("/holdings/{user_id}")
    public List<Holding> getHoldingById(@PathVariable Long user_id) {
        return holdingService.getHoldingsById(user_id);
    }

    @DeleteMapping("/holdings/{user_id}")
    public void deleteHolding(@RequestBody HoldingDTO holding) {
        holdingService.deleteHolding(holding.getUser_id(), holding.getCrypto_id());
    }
}
