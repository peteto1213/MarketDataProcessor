# Market Data Processor Simulation

## Table of Content
- About the project
- Assumptions
- Overall design
- Logic flow

## About the project
Market Data Process is a class that serves 2 functions:
1. receives real time market data from exchange
2. send the data as messages to other applications

### Method - onMessage
- responsible for function 1, called by a single thread at unknown rate per second
- each symbol should not have more than 1 update per second

### Method - publishAggregatedMarketData
- responsible for function 2
- each symbol should have the latest market data when published
- this method should not be called more than 100 times per second

## Assumptions
1. The MarketDataProcess class should always have only one instance, to ensure single source of truth
2. Enquiry of market data on client side is by symbol, which is String data type
3. All latest market data is retrieved and stored from/at a concurrent HashMap in this class
4. The raw data from the exchange shall share similar data structure and field names with the MarketData class

## Overall design
![Overall design](./images/Market%20Data%20Throttle%20Control.jpg)

## Logic flow
To implement the rate limiting situation in this project, the logic flow is as followed:
### grant update access
![grant update access](./images/grant%20update%20access.jpg)

### grant publish access
![grant publish access](./images/grant%20publish%20access.jpg)