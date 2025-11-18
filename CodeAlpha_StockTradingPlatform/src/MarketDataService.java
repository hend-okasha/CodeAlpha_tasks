import java.util.List;
import java.util.Random;

/**
 * Simulates market data and price fluctuations
 * In a real-world scenario, this would integrate with financial data APIs
 *
 * @author Hend Okasha
 */
public class MarketDataService {
    private final Random random;

    public MarketDataService() {
        this.random = new Random();
    }


    public void updateMarketPrices(List<Stock> stocks) {
        for (Stock stock : stocks) {
            double currentPrice = stock.getCurrentPrice();

            // Generate random price change between -5% and +5%
            double changePercent = (random.nextDouble() * 10) - 5;
            double newPrice = currentPrice * (1 + changePercent / 100);

            // Ensure price doesn't go below $1
            if (newPrice < 1.0) {
                newPrice = 1.0;
            }

            stock.setCurrentPrice(newPrice);
        }
    }


    public void displayMarketData(List<Stock> stocks) {
        System.out.println("\n─────────────────────────────────────────");
        System.out.println("              Market Data");
        System.out.println("─────────────────────────────────────────");
        System.out.printf("%-8s %-20s %10s %12s%n", "Symbol", "Company", "Price", "Change");
        System.out.println("─────────────────────────────────────────");

        for (Stock stock : stocks) {
            double changePercent = stock.getPriceChangePercent();
            String changeStr = String.format("%+.2f%%", changePercent);

            System.out.printf("%-8s %-20s $%9.2f %12s%n",
                    stock.getSymbol(),
                    truncateString(stock.getName(), 20),
                    stock.getCurrentPrice(),
                    changeStr);
        }

        System.out.println("─────────────────────────────────────────");
    }


    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}
