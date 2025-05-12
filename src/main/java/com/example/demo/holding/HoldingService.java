package com.example.demo.holding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoldingService {
    @Autowired
    HoldingRepository holdingRepository;

    public void saveHolding(Long user_id, Long crypto_id, double quantity) {
        HoldingDTO holding = new HoldingDTO(user_id, crypto_id, quantity);
        holdingRepository.save(holding, quantity);
    }

    public List<Holding> getHoldingsById(Long user_id) {
        return holdingRepository.findById(user_id)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public void deleteHolding(Long user_id, Long crypto_id) {
        holdingRepository.deleteById(user_id, crypto_id);
    }
}
