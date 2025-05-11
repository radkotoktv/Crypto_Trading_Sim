package com.example.demo.kraken.price;

import com.example.demo.kraken.assetpairs.KrakenAssetPairService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KrakenPriceService {
    KrakenAssetPairService krakenAssetPairService;
    private final ExecutorService executorService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public KrakenPriceService(SimpMessagingTemplate messagingTemplate, KrakenAssetPairService krakenAssetPairService) {
        this.krakenAssetPairService = krakenAssetPairService;
        this.messagingTemplate = messagingTemplate;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @PostConstruct
    public void connectToKraken() {
        executorService.execute(() -> {
            try {
                WebSocketClient client = new KrakenWebSocketClient(new URI("wss://ws.kraken.com"), krakenAssetPairService, messagingTemplate);
                client.setSocketFactory(SSLSocketFactory.getDefault());
                client.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    @PreDestroy
    public void cleanup() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
