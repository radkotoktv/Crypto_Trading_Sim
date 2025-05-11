package com.example.demo.kraken.price;

import java.time.LocalDateTime;
import java.util.Objects;

public class Price implements Comparable<Price> {
    private String symbol;
    private String price;
    private LocalDateTime timestamp;

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(symbol, price1.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public int compareTo(Price other) {
        int priceComparison = Double.compare(Double.parseDouble(this.price), Double.parseDouble(other.price));
        if (priceComparison != 0) {
            return priceComparison;
        }
        return this.symbol.compareTo(other.symbol);
    }
}