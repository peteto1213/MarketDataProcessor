package main;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MarketDataProcessor implements RateLimiter {

    // To ensure single source of information
    private static MarketDataProcessor single_instance = null;

    // All data to be sent should be from one HashMap
    private ConcurrentHashMap<String, MarketData> marketDataMap = new ConcurrentHashMap<>();

    // Implementation of token bucket
    private final int DATA_CAPACITY = 100;
    private final int TIME_WINDOW = 1000;

    private AtomicInteger currentCapacity = new AtomicInteger(DATA_CAPACITY);
    private AtomicLong lastPublishTime = new AtomicLong(System.currentTimeMillis());
    private AtomicLong lastMessageUpdateTime = new AtomicLong(System.currentTimeMillis());

    // private constructor to ensure class with single instance
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
        if(grantUpdateAccess(data)){
            marketDataMap.put(data.getSymbol(), data);
            System.out.println(Thread.currentThread().getName() + " -> Data received and updated");
        }else{
            System.out.println(Thread.currentThread().getName() + " -> Each symbol can not have more than 1 update per second");
        }

    }

    // Condition 3: Cannot be called more than 100 times per second
    public void publishAggregatedMarketData(MarketData data){
        //Do Nothing, assume implemented
        if(grantPublishAccess()){
            System.out.println("Market Data: " + marketDataMap.get(data.getSymbol()));
            System.out.println(Thread.currentThread().getName() + " -> market data published successfully!");
        }else{
            System.out.println(Thread.currentThread().getName() + " -> too many requests of market data, please try again later!");
        }

    }

    /**
     * @description check if there is any data publish capacity
     * @return boolean
     */
    @Override
    public boolean grantPublishAccess() {
        refreshCapacity();

        if(currentCapacity.get() > 0){
            currentCapacity.decrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * @description refresh data publish capacity if 1 second has passed
     */
    void refreshCapacity(){
        long currentTime = System.currentTimeMillis();

        if(currentTime - lastPublishTime.get() > TIME_WINDOW){
            currentCapacity.getAndSet(DATA_CAPACITY);
            lastPublishTime.getAndSet(currentTime);
        }
    }

    /**
     * @description only allow update if the data is not in the HashMap or 1 second has passed
     * @param data
     * @return boolean
     */
    @Override
    public boolean grantUpdateAccess(MarketData data) {
        long currentTime = System.currentTimeMillis();

        if(currentTime - lastMessageUpdateTime.get() > TIME_WINDOW || !(marketDataMap.containsKey(data.getSymbol()))){
            lastMessageUpdateTime.getAndSet(currentTime);
            return true;
        }

        return false;
    }
}
