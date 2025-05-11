package com.example.demo.kraken.price;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PriceController {
    private final Map<String, Double> priceMap;

    public PriceController(Map<String, Double> priceMap) {
        this.priceMap = priceMap;
    }

    @GetMapping("/top-prices")
    public List<Map.Entry<String, Double>> getTop20Prices() {
        List<Map.Entry<String, Double>> list = new ArrayList<>(priceMap.entrySet());
        list.sort(Map.Entry.comparingByValue());
        return list.stream()
                .limit(20)
                .collect(Collectors.toList());
    }
}
