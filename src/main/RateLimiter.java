package main;

public interface RateLimiter {
    boolean grantPublishAccess();

    boolean grantUpdateAccess(MarketData data);
}
