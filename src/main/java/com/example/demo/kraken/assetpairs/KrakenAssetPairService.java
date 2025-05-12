package com.example.demo.kraken.assetpairs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KrakenAssetPairService {
    private static final String ASSETS_URL = "https://api.kraken.com/0/public/Assets";
    private final KrakenAssetPairRepository krakenAssetPairRepository;

    @Autowired
    public KrakenAssetPairService(KrakenAssetPairRepository krakenAssetPairRepository) {
        this.krakenAssetPairRepository = krakenAssetPairRepository;
    }

    public List<String> getAllCurrencies() {
        List<String> currencies = new ArrayList<>();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ASSETS_URL))
                    .header("Content-Type", "application/json")
                    .build();

            String response = httpClient.send(request,
                                            HttpResponse.BodyHandlers.ofString()).body();

            JsonObject assetPairs = JsonParser.parseString(response)
                    .getAsJsonObject()
                    .getAsJsonObject("result");

            for (Map.Entry<String, JsonElement> entry : assetPairs.entrySet()) {
                JsonObject pairInfo = entry.getValue().getAsJsonObject();
                String status = pairInfo.get("status").getAsString();
                String altName = pairInfo.get("altname").getAsString();

                if (status.equals("enabled") && !altName.equals("USD")) {
                    currencies.add(altName);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return currencies;
    }

    public List<String> getAllTradingPairs() {
        List<String> currencies = getAllCurrencies();
        List<String> pairs = new ArrayList<>();
        for (String currency : currencies) {
            pairs.add(currency + "/USD");
        }
        return pairs;
    }

    public int updateCrypto() {
        List<String> cryptoList = getAllCurrencies();
        int count = 0;
        for (String crypto : cryptoList) {
            if (krakenAssetPairRepository.save(crypto)) {
                count++;
            }
        }
        return count;
    }

    public Long getIdByName(String name) {
        return krakenAssetPairRepository.findIdByName(name);
    }

    public String getNameById(Long id) {
        return krakenAssetPairRepository.findNameById(id);
    }
}
