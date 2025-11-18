/**
 * Represents a stock in the trading platform with its properties
 *
 * @author Hend Okasha
 */
public class Stock {
    private String symbol;
    private String name;
    private double currentPrice;
    private double previousPrice;


    public Stock(String symbol, String name, double currentPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.previousPrice = currentPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getPreviousPrice() {
        return previousPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.previousPrice = this.currentPrice;
        this.currentPrice = currentPrice;
    }


    public double getPriceChangePercent() {
        if (previousPrice == 0) {
            return 0;
        }
        return ((currentPrice - previousPrice) / previousPrice) * 100;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", previousPrice=" + previousPrice +
                '}';
    }
}
