import java.time.LocalDate;

/**
 * Represents a stock holding in a user's portfolio
 * Contains information about owned shares
 *
 * @author Hend Okasha
 */
public class StockHolding  {
    private String symbol;
    private int quantity;
    private double purchasePrice;
    private LocalDate purchaseDate;


    public StockHolding(String symbol, int quantity, double purchasePrice, LocalDate purchaseDate) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getTotalInvestment() {
        return quantity * purchasePrice;
    }

    @Override
    public String toString() {
        return "StockHolding{" +
                "symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                ", purchasePrice=" + purchasePrice +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
