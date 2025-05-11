package com.example.demo.kraken.assetpairs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KrakenAssetPairController {
    @Autowired
    KrakenAssetPairService krakenAssetPairService;

    @RequestMapping("/assetpairs")
    public void updateDatabase() {
        int amount = krakenAssetPairService.updateCrypto();
        System.out.println("Amount of new asset pairs: " + amount);
    }
}
