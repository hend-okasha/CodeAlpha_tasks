import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the trading platform
 * Contains user information and portfolio holdings
 *
 * @author Hend Okasha
 */
public class User  {
    private int userId;
    private String username;
    private double balance;
    private List<StockHolding> portfolio;


    public User(int userId, String username, double balance) {
        this.userId = userId;
        this.username = username;
        this.balance = balance;
        this.portfolio = new ArrayList<>();
    }


    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public List<StockHolding> getPortfolio() {
        return portfolio;
    }


    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }


    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }


    public StockHolding getHolding(String symbol) {
        for (StockHolding holding : portfolio) {
            if (holding.getSymbol().equalsIgnoreCase(symbol)) {
                return holding;
            }
        }
        return null;
    }


    public void addHolding(StockHolding holding) {
        portfolio.add(holding);
    }


    public void removeHolding(StockHolding holding) {
        portfolio.remove(holding);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                ", portfolio=" + portfolio +
                '}';
    }
}
