package com.example.demo.kraken.price;

import com.example.demo.kraken.assetpairs.KrakenAssetPairService;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Queue;

public class KrakenWebSocketClient extends WebSocketClient {
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    private final KrakenAssetPairService krakenAssetPairService;
    private final Queue<Price> minHeap;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public KrakenWebSocketClient(URI serverUri, KrakenAssetPairService krakenAssetPairService, SimpMessagingTemplate messagingTemplate) {
        super(serverUri);
        this.krakenAssetPairService = krakenAssetPairService;
        this.minHeap = new PriorityQueue<>();
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(String message) {
        if (message.contains("\"c\":")) {
            String price = extractPrice(message);
            String name = extractName(message);
            if (price != null && name != null) {
                Price priceObj = new Price();
                priceObj.setSymbol(name);
                priceObj.setPrice(price);
                priceObj.setTimestamp(LocalDateTime.now());
                synchronized (minHeap) {
                    insertIntoMinHeap(priceObj);
                }
                messagingTemplate.convertAndSend("/topic/price", minHeap);
            }
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to Kraken.");
        String text = """
                            {
                              "event": "subscribe",
                              "pair": "PAIRS",
                              "subscription": { "name": "ticker" }
                            }
                            """;
        String joinedPairs = "\"" + String.join("\", \"", krakenAssetPairService.getAllTradingPairs()) + "\"";
        text = text.replace("\"PAIRS\"", "[" + joinedPairs + "]");
        send(text);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection closed: " + s);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    private String extractPrice(String json) {
        try {
            int cIndex = json.indexOf("\"c\":[\"");
            if (cIndex == -1) return null;
            int start = cIndex + 6;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractName(String json) {
        try {
            int tickerIndex = json.indexOf("\"ticker\"");
            if (tickerIndex == -1) return null;
            int start = tickerIndex + 10;
            int end = json.indexOf("/", start);
            if (end == -1) return null;

            return json.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    private synchronized void insertIntoMinHeap(Price price) {
        if (minHeap.contains(price)) {
            minHeap.remove(price);
            minHeap.add(price);
        } else {
            if (minHeap.size() < 20) {
                minHeap.add(price);
            } else {
                double min = Double.parseDouble(minHeap.peek().getPrice());
                if (Double.parseDouble(price.getPrice()) > min) {
                    minHeap.poll();
                    minHeap.add(price);
                }
            }
        }
    }
}
