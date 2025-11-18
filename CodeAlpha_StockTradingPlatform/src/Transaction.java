import java.time.LocalDateTime;

/**
 * Represents a trading transaction record
 * Contains all information about a buy or sell operation
 *
 * @author Hend Okasha
 */
public class Transaction {
    private int transactionId;
    private int userId;
    private String symbol;
    private TransactionType type;
    private int quantity;
    private double pricePerShare;
    private double totalAmount;
    private LocalDateTime timestamp;


    public Transaction(int transactionId, int userId, String symbol, TransactionType type,
                       int quantity, double pricePerShare, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.symbol = symbol;
        this.type = type;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.totalAmount = quantity * pricePerShare;
        this.timestamp = timestamp;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public TransactionType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", userId=" + userId +
                ", symbol='" + symbol + '\'' +
                ", type=" + type +
                ", quantity=" + quantity +
                ", pricePerShare=" + pricePerShare +
                ", totalAmount=" + totalAmount +
                ", timestamp=" + timestamp +
                '}';
    }
}
