import main.MarketData;
import main.MarketDataProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MarketDataProcessor processor = MarketDataProcessor.getInstance();

        MarketData data = new MarketData("AAPL", 100.0, 100.0, 100.0);

        ExecutorService executors = Executors.newFixedThreadPool(300);
        for(int i = 0; i < 300; i++) {
            executors.execute(() -> processor.onMessage(data));
            Thread.sleep(2);
        }

        ExecutorService executors2 = Executors.newFixedThreadPool(300);
        for(int i = 0; i < 300; i++) {
            executors2.execute(() -> processor.publishAggregatedMarketData(data));
            Thread.sleep(2);
        }


        System.exit(1);
    }
}
