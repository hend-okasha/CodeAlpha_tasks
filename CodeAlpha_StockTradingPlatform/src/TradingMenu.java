import java.util.List;
import java.util.Scanner;

/**
 * User Interface for Stock Trading Platform
 * Handles all user interactions and menu operations
 *
 * @author Hend Okasha
 */
public class TradingMenu {
    private final TradingPlatform platform;
    private final MarketDataService marketService;
    private final Scanner scanner;
    private User currentUser;

    public TradingMenu(TradingPlatform platform, MarketDataService marketService) {
        this.platform = platform;
        this.marketService = marketService;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }


    public void start() {

        System.out.println("  Welcome to Stock Trading Platform");


        if (!loginOrRegister()) {
            System.out.println("Exiting system. Goodbye!");
            scanner.close();
            return;
        }

        // Main menu loop
        while (true) {
            displayMainMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    viewMarketDataFlow();
                    break;
                case "2":
                    buyStockFlow();
                    break;
                case "3":
                    sellStockFlow();
                    break;
                case "4":
                    viewPortfolioFlow();
                    break;
                case "5":
                    viewTransactionHistoryFlow();
                    break;
                case "6":
                    depositFundsFlow();
                    break;
                case "7":
                    exitProgram();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private boolean loginOrRegister() {
        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register New Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (loginFlow()) {
                        return true;
                    }
                    break;
                case "2":
                    if (registerFlow()) {
                        return true;
                    }
                    break;
                case "3":
                    return false;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }


    private boolean loginFlow() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }

        User user = platform.getUserByUsername(username);
        if (user == null) {
            System.out.println("User not found.");
            return false;
        }

        currentUser = user;
        System.out.println("Login successful! Welcome, " + currentUser.getUsername());
        return true;
    }


    private boolean registerFlow() {
        try {
            System.out.println("\n--- Register New Account ---");
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
                return false;
            }

            System.out.print("Enter initial deposit amount: $");
            double initialBalance = Double.parseDouble(scanner.nextLine().trim());

            User newUser = platform.registerUser(username, initialBalance);
            if (newUser != null) {
                currentUser = newUser;
                System.out.println("\nAccount created successfully!");
                System.out.println("User ID: " + newUser.getUserId());
                System.out.println("Welcome, " + newUser.getUsername() + "!");
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
        return false;
    }


    private void displayMainMenu() {
        System.out.println("\n─────────────────────────────────────────");
        System.out.println("   Stock Trading Platform");
        System.out.println("   User: " + currentUser.getUsername());
        System.out.println("─────────────────────────────────────────");
        System.out.println("  1. View Market Data");
        System.out.println("  2. Buy Stock");
        System.out.println("  3. Sell Stock");
        System.out.println("  4. View Portfolio");
        System.out.println("  5. View Transaction History");
        System.out.println("  6. Deposit Funds");
        System.out.println("  7. Exit");
        System.out.println("─────────────────────────────────────────");
        System.out.printf("Current Balance: $%.2f%n", currentUser.getBalance());
        System.out.print("Choose an option: ");
    }


    private void viewMarketDataFlow() {
        System.out.println("\n--- Updating Market Data ---");
        marketService.updateMarketPrices(platform.getStocks());
        marketService.displayMarketData(platform.getStocks());
        waitForEnter();
    }


    private void buyStockFlow() {
        try {
            System.out.println("\n--- Buy Stock ---");

            marketService.displayMarketData(platform.getStocks());

            System.out.print("\nEnter stock symbol: ");
            String symbol = scanner.nextLine().trim().toUpperCase();

            Stock stock = platform.getStockBySymbol(symbol);
            if (stock == null) {
                System.out.println("Stock symbol not found.");
                waitForEnter();
                return;
            }

            System.out.printf("Current price of %s: $%.2f%n", symbol, stock.getCurrentPrice());
            System.out.print("Enter quantity to buy: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                waitForEnter();
                return;
            }

            double totalCost = stock.getCurrentPrice() * quantity;

            System.out.println("\n─────────────────────────────────────────");
            System.out.printf("Stock: %s%n", symbol);
            System.out.printf("Quantity: %d shares%n", quantity);
            System.out.printf("Price per share: $%.2f%n", stock.getCurrentPrice());
            System.out.printf("Total cost: $%.2f%n", totalCost);
            System.out.printf("Your balance: $%.2f%n", currentUser.getBalance());
            System.out.println("─────────────────────────────────────────");

            System.out.print("Confirm purchase? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("yes")) {
                System.out.println("Purchase cancelled.");
                waitForEnter();
                return;
            }

            Transaction transaction = platform.buyStock(currentUser, symbol, quantity);
            if (transaction != null) {
                System.out.println("\nPurchase successful!");
                System.out.println("Transaction ID: " + transaction.getTransactionId());
                System.out.printf("New balance: $%.2f%n", currentUser.getBalance());
            } else {
                System.out.println("Purchase failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
        waitForEnter();
    }


    private void sellStockFlow() {
        try {
            System.out.println("\n--- Sell Stock ---");

            if (currentUser.getPortfolio().isEmpty()) {
                System.out.println("You don't own any stocks.");
                waitForEnter();
                return;
            }

            displayPortfolioHoldings();

            System.out.print("\nEnter stock symbol to sell: ");
            String symbol = scanner.nextLine().trim().toUpperCase();

            StockHolding holding = currentUser.getHolding(symbol);
            if (holding == null) {
                System.out.println("You don't own any shares of " + symbol + ".");
                waitForEnter();
                return;
            }

            Stock stock = platform.getStockBySymbol(symbol);
            if (stock == null) {
                System.out.println("Stock not found in market.");
                waitForEnter();
                return;
            }

            System.out.printf("You own %d shares of %s%n", holding.getQuantity(), symbol);
            System.out.printf("Current market price: $%.2f%n", stock.getCurrentPrice());
            System.out.print("Enter quantity to sell: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                waitForEnter();
                return;
            }

            double totalValue = stock.getCurrentPrice() * quantity;

            System.out.println("\n─────────────────────────────────────────");
            System.out.printf("Stock: %s%n", symbol);
            System.out.printf("Quantity: %d shares%n", quantity);
            System.out.printf("Price per share: $%.2f%n", stock.getCurrentPrice());
            System.out.printf("Total value: $%.2f%n", totalValue);
            System.out.printf("Purchase price: $%.2f%n", holding.getPurchasePrice());
            double profitLoss = (stock.getCurrentPrice() - holding.getPurchasePrice()) * quantity;
            System.out.printf("Profit/Loss: $%.2f%n", profitLoss);
            System.out.println("─────────────────────────────────────────");

            System.out.print("Confirm sale? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("yes")) {
                System.out.println("Sale cancelled.");
                waitForEnter();
                return;
            }

            Transaction transaction = platform.sellStock(currentUser, symbol, quantity);
            if (transaction != null) {
                System.out.println("\nSale successful!");
                System.out.println("Transaction ID: " + transaction.getTransactionId());
                System.out.printf("New balance: $%.2f%n", currentUser.getBalance());
            } else {
                System.out.println("Sale failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
        waitForEnter();
    }


    private void viewPortfolioFlow() {
        System.out.println("\n─────────────────────────────────────────");
        System.out.println("         Your Portfolio");
        System.out.println("─────────────────────────────────────────");
        System.out.printf("Account Balance: $%.2f%n", currentUser.getBalance());
        System.out.println("─────────────────────────────────────────");

        if (currentUser.getPortfolio().isEmpty()) {
            System.out.println("No holdings yet");
            System.out.println("─────────────────────────────────────────");
            waitForEnter();
            return;
        }

        System.out.printf("%-8s %8s %12s %12s %14s%n",
                "Symbol", "Shares", "Avg Cost", "Current", "Value");
        System.out.println("─────────────────────────────────────────");

        double totalValue = 0;

        for (StockHolding holding : currentUser.getPortfolio()) {
            Stock stock = platform.getStockBySymbol(holding.getSymbol());
            if (stock != null) {
                double currentValue = stock.getCurrentPrice() * holding.getQuantity();
                totalValue += currentValue;

                System.out.printf("%-8s %8d $%11.2f $%11.2f $%13.2f%n",
                        holding.getSymbol(),
                        holding.getQuantity(),
                        holding.getPurchasePrice(),
                        stock.getCurrentPrice(),
                        currentValue);
            }
        }

        System.out.println("─────────────────────────────────────────");

        double[] performance = platform.calculatePortfolioPerformance(currentUser);
        double totalInvestment = performance[0];
        double currentValue = performance[1];
        double profitLoss = performance[2];
        double profitLossPercent = performance[3];

        System.out.printf("Total Investment:    $%.2f%n", totalInvestment);
        System.out.printf("Current Value:       $%.2f%n", currentValue);
        System.out.printf("Profit/Loss:         $%.2f%n", profitLoss);
        System.out.printf("Return:              %.2f%%%n", profitLossPercent);
        System.out.println("─────────────────────────────────────────");
        System.out.printf("Total Net Worth:     $%.2f%n",
                currentUser.getBalance() + currentValue);
        System.out.println("─────────────────────────────────────────");

        waitForEnter();
    }


    private void viewTransactionHistoryFlow() {
        System.out.println("\n─────────────────────────────────────────");
        System.out.println("       Transaction History");
        System.out.println("─────────────────────────────────────────");

        List<Transaction> userTransactions = platform.getUserTransactions(currentUser.getUserId());

        if (userTransactions.isEmpty()) {
            System.out.println("No transactions yet");
            System.out.println("─────────────────────────────────────────");
            waitForEnter();
            return;
        }

        System.out.printf("%-4s %-8s %-6s %8s %10s %16s%n",
                "ID", "Symbol", "Type", "Qty", "Price", "Date");
        System.out.println("─────────────────────────────────────────");

        for (Transaction transaction : userTransactions) {
            String date = transaction.getTimestamp().toString().substring(0, 16);
            System.out.printf("%-4d %-8s %-6s %8d $%9.2f %16s%n",
                    transaction.getTransactionId(),
                    transaction.getSymbol(),
                    transaction.getType(),
                    transaction.getQuantity(),
                    transaction.getPricePerShare(),
                    date);
        }

        System.out.println("─────────────────────────────────────────");
        waitForEnter();
    }


    private void depositFundsFlow() {
        try {
            System.out.println("\n--- Deposit Funds ---");
            System.out.printf("Current balance: $%.2f%n", currentUser.getBalance());
            System.out.print("Enter amount to deposit: $");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            if (currentUser.deposit(amount)) {
                System.out.println("\nDeposit successful!");
                System.out.printf("New balance: $%.2f%n", currentUser.getBalance());
            } else {
                System.out.println("Deposit failed. Amount must be positive.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
        waitForEnter();
    }


    private void displayPortfolioHoldings() {
        System.out.println("\nYour holdings:");
        System.out.println("─────────────────────────────────────────");

        for (StockHolding holding : currentUser.getPortfolio()) {
            Stock stock = platform.getStockBySymbol(holding.getSymbol());
            if (stock != null) {
                System.out.printf("%s: %d shares @ $%.2f (Current: $%.2f)%n",
                        holding.getSymbol(),
                        holding.getQuantity(),
                        holding.getPurchasePrice(),
                        stock.getCurrentPrice());
            }
        }

        System.out.println("─────────────────────────────────────────");
    }


    private void waitForEnter() {
        System.out.print("\nPress Enter to return to menu...");
        scanner.nextLine();
    }


    private void exitProgram() {
        System.out.println("\nThank you for using Stock Trading Platform!");
        System.out.println("Goodbye!");
        scanner.close();
        System.exit(0);
    }
}
