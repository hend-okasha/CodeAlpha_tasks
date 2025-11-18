import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Stock Trading Platform Management System
 * Handles user accounts, stock inventory, and trading operations
 *
 * @author Hend Okasha
 */
public class TradingPlatform {
    private List<User> users;
    private List<Stock> stocks;
    private List<Transaction> transactions;
    private final String usersFile = "users.txt";
    private final String stocksFile = "stocks.txt";
    private final String transactionsFile = "transactions.txt";
    private final String holdingsFile = "holdings.txt";


    public TradingPlatform() {
        users = new ArrayList<>();
        stocks = new ArrayList<>();
        transactions = new ArrayList<>();
        loadStocks();
        loadUsers();
        loadHoldings();
        loadTransactions();
    }


    public User registerUser(String username, double initialBalance) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists.");
                return null;
            }
        }

        if (initialBalance < 0) {
            System.out.println("Initial balance cannot be negative.");
            return null;
        }

        int userId = users.stream()
                .map(User::getUserId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        User newUser = new User(userId, username, initialBalance);
        users.add(newUser);
        saveUsers();
        return newUser;
    }


    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }


    public Stock getStockBySymbol(String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                return stock;
            }
        }
        return null;
    }


    public Transaction buyStock(User user, String symbol, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return null;
        }

        Stock stock = getStockBySymbol(symbol);
        if (stock == null) {
            System.out.println("Stock symbol not found.");
            return null;
        }

        double totalCost = stock.getCurrentPrice() * quantity;

        if (user.getBalance() < totalCost) {
            System.out.println("Insufficient balance. Required: $" + String.format("%.2f", totalCost));
            return null;
        }


        user.withdraw(totalCost);


        StockHolding existingHolding = user.getHolding(symbol);
        if (existingHolding != null) {
            int oldQuantity = existingHolding.getQuantity();
            double oldPrice = existingHolding.getPurchasePrice();
            int newQuantity = oldQuantity + quantity;
            double avgPrice = ((oldPrice * oldQuantity) + (stock.getCurrentPrice() * quantity)) / newQuantity;

            existingHolding.setQuantity(newQuantity);
        } else {
            StockHolding newHolding = new StockHolding(symbol, quantity,
                    stock.getCurrentPrice(), LocalDate.now());
            user.addHolding(newHolding);
        }

        int transactionId = transactions.stream()
                .map(Transaction::getTransactionId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Transaction transaction = new Transaction(transactionId, user.getUserId(),
                symbol, TransactionType.BUY, quantity, stock.getCurrentPrice(),
                LocalDateTime.now());

        transactions.add(transaction);

        saveUsers();
        saveHoldings();
        saveTransactions();
        return transaction;
    }


    public Transaction sellStock(User user, String symbol, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return null;
        }

        Stock stock = getStockBySymbol(symbol);
        if (stock == null) {
            System.out.println("Stock symbol not found.");
            return null;
        }

        StockHolding holding = user.getHolding(symbol);
        if (holding == null) {
            System.out.println("You don't own any shares of " + symbol + ".");
            return null;
        }

        if (holding.getQuantity() < quantity) {
            System.out.println("Insufficient shares. You own " + holding.getQuantity() + " shares.");
            return null;
        }

        double totalValue = stock.getCurrentPrice() * quantity;


        user.deposit(totalValue);


        int remainingQuantity = holding.getQuantity() - quantity;
        if (remainingQuantity == 0) {
            user.removeHolding(holding);
        } else {
            holding.setQuantity(remainingQuantity);
        }

        int transactionId = transactions.stream()
                .map(Transaction::getTransactionId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Transaction transaction = new Transaction(transactionId, user.getUserId(),
                symbol, TransactionType.SELL, quantity, stock.getCurrentPrice(),
                LocalDateTime.now());

        transactions.add(transaction);

        saveUsers();
        saveHoldings();
        saveTransactions();
        return transaction;
    }


    public double calculatePortfolioValue(User user) {
        double totalValue = 0;

        for (StockHolding holding : user.getPortfolio()) {
            Stock stock = getStockBySymbol(holding.getSymbol());
            if (stock != null) {
                totalValue += stock.getCurrentPrice() * holding.getQuantity();
            }
        }

        return totalValue;
    }


    public double[] calculatePortfolioPerformance(User user) {
        double totalInvestment = 0;
        double currentValue = 0;

        for (StockHolding holding : user.getPortfolio()) {
            totalInvestment += holding.getTotalInvestment();

            Stock stock = getStockBySymbol(holding.getSymbol());
            if (stock != null) {
                currentValue += stock.getCurrentPrice() * holding.getQuantity();
            }
        }

        double profitLoss = currentValue - totalInvestment;
        double profitLossPercent = (totalInvestment > 0) ?
                (profitLoss / totalInvestment) * 100 : 0;

        return new double[]{totalInvestment, currentValue, profitLoss, profitLossPercent};
    }


    public List<Transaction> getUserTransactions(int userId) {
        List<Transaction> userTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getUserId() == userId) {
                userTransactions.add(transaction);
            }
        }

        return userTransactions;
    }


    public List<User> getUsers() {
        return users;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }


    private void loadStocks() {
        stocks.clear();
        File file = new File(stocksFile);

        if (!file.exists()) {
            System.out.println("stocks.txt not found — starting with empty stocks list.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 4) {
                    continue;
                }

                String symbol = parts[0].trim();
                String name = parts[1].trim();
                double currentPrice = Double.parseDouble(parts[2].trim());
                double previousPrice = Double.parseDouble(parts[3].trim());

                Stock stock = new Stock(symbol, name, currentPrice);
                stock.setCurrentPrice(currentPrice);
                stocks.add(stock);
            }
        } catch (IOException e) {
            System.out.println("Error loading stocks: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid stock data in file: " + e.getMessage());
        }
    }


    private void saveStocks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(stocksFile))) {
            for (Stock stock : stocks) {
                pw.println(stock.getSymbol() + "," +
                        stock.getName() + "," +
                        stock.getCurrentPrice() + "," +
                        stock.getPreviousPrice());
            }
        } catch (IOException e) {
            System.out.println("Error saving stocks: " + e.getMessage());
        }
    }


    private void loadUsers() {
        users.clear();
        File file = new File(usersFile);

        if (!file.exists()) {
            System.out.println("users.txt not found — starting with empty users list.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) {
                    continue;
                }

                int userId = Integer.parseInt(parts[0].trim());
                String username = parts[1].trim();
                double balance = Double.parseDouble(parts[2].trim());

                users.add(new User(userId, username, balance));
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid user data in file: " + e.getMessage());
        }
    }


    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(usersFile))) {
            for (User user : users) {
                pw.println(user.getUserId() + "," +
                        user.getUsername() + "," +
                        user.getBalance());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }


    private void loadHoldings() {
        File file = new File(holdingsFile);

        if (!file.exists()) {
            System.out.println("holdings.txt not found — starting with empty holdings.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 5) {
                    continue;
                }

                int userId = Integer.parseInt(parts[0].trim());
                String symbol = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());
                double purchasePrice = Double.parseDouble(parts[3].trim());
                LocalDate purchaseDate = LocalDate.parse(parts[4].trim());

                for (User user : users) {
                    if (user.getUserId() == userId) {
                        StockHolding holding = new StockHolding(symbol, quantity,
                                purchasePrice, purchaseDate);
                        user.addHolding(holding);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading holdings: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date in holdings file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid holdings data in file: " + e.getMessage());
        }
    }


    private void saveHoldings() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(holdingsFile))) {
            for (User user : users) {
                for (StockHolding holding : user.getPortfolio()) {
                    pw.println(user.getUserId() + "," +
                            holding.getSymbol() + "," +
                            holding.getQuantity() + "," +
                            holding.getPurchasePrice() + "," +
                            holding.getPurchaseDate());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving holdings: " + e.getMessage());
        }
    }


    private void loadTransactions() {
        transactions.clear();
        File file = new File(transactionsFile);

        if (!file.exists()) {
            System.out.println("transactions.txt not found — starting with empty transactions list.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 7) {
                    continue;
                }

                int transactionId = Integer.parseInt(parts[0].trim());
                int userId = Integer.parseInt(parts[1].trim());
                String symbol = parts[2].trim();
                TransactionType type = TransactionType.valueOf(parts[3].trim().toUpperCase());
                int quantity = Integer.parseInt(parts[4].trim());
                double pricePerShare = Double.parseDouble(parts[5].trim());
                LocalDateTime timestamp = LocalDateTime.parse(parts[6].trim());

                transactions.add(new Transaction(transactionId, userId, symbol,
                        type, quantity, pricePerShare, timestamp));
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid timestamp in transactions file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid transaction type in file: " + e.getMessage());
        }
    }


    private void saveTransactions() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(transactionsFile))) {
            for (Transaction transaction : transactions) {
                pw.println(transaction.getTransactionId() + "," +
                        transaction.getUserId() + "," +
                        transaction.getSymbol() + "," +
                        transaction.getType() + "," +
                        transaction.getQuantity() + "," +
                        transaction.getPricePerShare() + "," +
                        transaction.getTimestamp());
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
}