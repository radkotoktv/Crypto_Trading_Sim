package com.example.demo.kraken.assetpairs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class KrakenAssetPairController {
    @Autowired
    KrakenAssetPairService krakenAssetPairService;

    @GetMapping("/assetpairs")
    public void updateDatabase() {
        int amount = krakenAssetPairService.updateCrypto();
        System.out.println("Amount of new asset pairs: " + amount);
    }

    @GetMapping("/id")
    public Long getIdByName(@RequestParam String name) {
        return krakenAssetPairService.getIdByName(name);
    }

    @GetMapping("/name")
    public String getNameById(@RequestParam Long id) {
        return krakenAssetPairService.getNameById(id);
    }

}