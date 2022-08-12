package main;

import java.util.concurrent.ConcurrentHashMap;

public class MarketDataProcessor {

    // To ensure single source of information
    private static MarketDataProcessor single_instance = null;

    // All data to be sent should be from one HashMap
    private ConcurrentHashMap<String, MarketData> marketDataMap = new ConcurrentHashMap<>();

    private MarketDataProcessor(){

    }

    public static MarketDataProcessor getInstance(){
        if(single_instance == null){
            single_instance = new MarketDataProcessor();
        }

        return single_instance;
    }


    // Condition 1: Receive incoming market data at unknown rate per second
    // Condition 2: Each symbol will not have more than 1 update per second
    public void onMessage(MarketData data){
        // Make sure the key of the map is not null or empty string
        if(data.getSymbol() == null || data.getSymbol().equals("")){
            System.out.println("Invalid data, data id: " + data.getDataId());
        }else{
            //Always ensure the latest data in the HashMap
            marketDataMap.merge(data.getSymbol(), data, (oldData, newData) -> newData);
        }

    }

    // Condition 3: Cannot be called more than 100 times per second
    public void publishAggregatedMarketData(MarketData data){
        //Do Nothing, assume implemented
    }
}
