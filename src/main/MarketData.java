package main;

import java.time.LocalDateTime;
import java.util.UUID;

public class MarketData {

    private UUID dataId;
    private String symbol;
    private double lastSalePrice;
    private double bidPrice;
    private double askPrice;
    private LocalDateTime updateTime;

    public MarketData(String symbol, double lastSalePrice, double bidPrice, double askPrice){
        this.symbol = symbol;
        this.lastSalePrice = lastSalePrice;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.updateTime = LocalDateTime.now();
    }

    public String getSymbol(){
        return this.symbol;
    }

    public UUID getDataId() {
        return dataId;
    }
}
