# Stock Trading Platform

This Java application simulates a basic trading environment where users can view stock data, make trades, and track portfolio performance.  
Stock prices fluctuate automatically using a simple multithreading mechanism.

---

## Features

### Market Simulation
- Real-time price updates every second  
- Display current stock list with live changing prices  

### Trading Operations
- Buy stocks at the current market price  
- Sell stocks and calculate gains/losses  
- View transaction history with timestamps  

### Portfolio Management
- Track owned stocks, quantities, and total value  
- Portfolio and history saved using File I/O  

*Example:*  
- AAPL price: 175.50 → fluctuates to 174.80  
- User buys 2 shares → system deducts the total cost  
- History saved: “Bought 2 AAPL at 174.80”  

---

### System Architecture / Classes
- **Main.java**  
  Entry point of the application, initializes the trading system  
- **TradingPlatform.java**  
  Core trading logic: executes buy/sell operations, manages orders  
- **TradingMenu.java**  
  Handles user interface: menu navigation, input validation, and output display  
- **Stock.java**  
  Represents a stock entity with symbol, name, and price  
- **User.java**  
  Represents a user account with credentials and portfolio
- **StockHolding.java**  
  Represents a portfolio position with quantity and average price  
- **Transaction.java**  
  Records trade transactions including stock, user, and trade details  
- **MarketDataService.java**  
  Simulates stock prices and market updates

---

## Technologies
- Java  
- OOP  
- Multithreading  
- File I/O  
- Console I/O  

---

## How to Run
1. Open the project folder  
2. Run `Main.java`  
3. Use the menu to buy/sell and view market data  
