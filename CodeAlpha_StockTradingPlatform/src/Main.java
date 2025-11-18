//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/**
 * Stock Trading Platform - Main Entry Point
 * A comprehensive system for simulating stock trading operations
 *
 * @author Hend Okasha
 */
public class Main {
    public static void main(String[] args) {
        // Initialize system components
        TradingPlatform platform = new TradingPlatform();
        MarketDataService marketService = new MarketDataService();


        if (platform.getStocks().isEmpty()) {
            initializeSampleStocks(platform);
        }


        TradingMenu menu = new TradingMenu(platform, marketService);
        menu.start();
    }


    private static void initializeSampleStocks(TradingPlatform platform) {
        Stock[] sampleStocks = {
                new Stock("AAPL", "Apple Inc.", 175.50),
                new Stock("GOOGL", "Alphabet Inc.", 142.30),
                new Stock("MSFT", "Microsoft Corp.", 380.75),
                new Stock("AMZN", "Amazon.com Inc.", 145.20),
                new Stock("TSLA", "Tesla Inc.", 245.80),
                new Stock("META", "Meta Platforms Inc.", 325.60),
                new Stock("NVDA", "NVIDIA Corp.", 495.30),
                new Stock("JPM", "JPMorgan Chase", 155.90),
                new Stock("V", "Visa Inc.", 245.40),
                new Stock("WMT", "Walmart Inc.", 165.75)
        };

        for (Stock stock : sampleStocks) {
            platform.getStocks().add(stock);
        }

        System.out.println("Sample stocks initialized successfully.");
    }
}