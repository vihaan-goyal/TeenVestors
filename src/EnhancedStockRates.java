import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class EnhancedStockRates {
    private static Map<String, Double> cachedRates = new HashMap<>();
    private static Map<String, String> popularStocks = new HashMap<>();
    
    static {
        // Teen-friendly stock symbols with company names
        popularStocks.put("AAPL", "Apple Inc.");
        popularStocks.put("GOOGL", "Google (Alphabet)");
        popularStocks.put("TSLA", "Tesla");
        popularStocks.put("AMZN", "Amazon");
        popularStocks.put("MSFT", "Microsoft");
        popularStocks.put("NFLX", "Netflix");
        popularStocks.put("DIS", "Disney");
        popularStocks.put("NKE", "Nike");
        popularStocks.put("SBUX", "Starbucks");
        popularStocks.put("SPY", "S&P 500 ETF");
        popularStocks.put("QQQ", "Nasdaq ETF");
    }
    
    public static Map<String, String> getPopularStocks() {
        return popularStocks;
    }
    
    // Get current stock price
    public static double getCurrentStockPrice(String symbol) {
        if (cachedRates.containsKey(symbol)) {
            return cachedRates.get(symbol);
        }
        
        try {
            String apiKey = "OZNU3M6HUIMRQ25C"; // Your existing API key
            String urlStr = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
            
            URI uri = URI.create(urlStr);
            URL url = uri.toURL();
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            String json = response.toString();
            
            // Parse the current price from Global Quote
            String priceKey = "\"05. price\":\"";
            int startIndex = json.indexOf(priceKey);
            if (startIndex == -1) throw new Exception("Price not found");
            
            startIndex += priceKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            
            String priceStr = json.substring(startIndex, endIndex);
            double price = Double.parseDouble(priceStr);
            
            cachedRates.put(symbol, price);
            return price;
            
        } catch (Exception e) {
            System.out.println("Error fetching price for " + symbol + ": " + e.getMessage());
            return 0.0;
        }
    }
    
    // Calculate potential stock investment growth (simplified model)
    public static double calculateStockGrowth(String symbol, double initialInvestment, int years) {
        try {
            // Get historical performance (simplified - using average market return)
            double currentPrice = getCurrentStockPrice(symbol);
            if (currentPrice == 0) return initialInvestment;
            
            // Use historical average returns for different stock types
            double annualGrowthRate = getEstimatedGrowthRate(symbol);
            
            // Calculate shares that can be bought
            double sharesBought = initialInvestment / currentPrice;
            
            // Project future value
            double futurePrice = currentPrice * Math.pow(1 + annualGrowthRate, years);
            
            return sharesBought * futurePrice;
            
        } catch (Exception e) {
            System.out.println("Error calculating stock growth: " + e.getMessage());
            return initialInvestment; // Return original investment if error
        }
    }
    
    private static double getEstimatedGrowthRate(String symbol) {
        // Simplified growth rate estimates for educational purposes
        // In reality, you'd want historical data
        switch (symbol.toUpperCase()) {
            case "AAPL":
            case "GOOGL":
            case "MSFT":
                return 0.12; // 12% annual growth (tech stocks)
            case "TSLA":
                return 0.15; // 15% (high growth but volatile)
            case "DIS":
            case "NKE":
            case "SBUX":
                return 0.10; // 10% (established companies)
            case "SPY":
            case "QQQ":
                return 0.08; // 8% (market ETFs)
            default:
                return 0.08; // Default market rate
        }
    }
    
    // For educational purposes - show risk levels
    public static String getRiskLevel(String symbol) {
        switch (symbol.toUpperCase()) {
            case "SPY":
            case "QQQ":
                return "LOW (Diversified ETF)";
            case "AAPL":
            case "MSFT":
            case "GOOGL":
                return "MEDIUM (Established Tech)";
            case "TSLA":
                return "HIGH (Growth Stock)";
            case "DIS":
            case "NKE":
            case "SBUX":
                return "MEDIUM (Consumer Brands)";
            default:
                return "MEDIUM";
        }
    }
    
    // Educational method - explain what the company does
    public static String getCompanyDescription(String symbol) {
        switch (symbol.toUpperCase()) {
            case "AAPL":
                return "Makes iPhones, iPads, Macs. You probably use their products daily!";
            case "GOOGL":
                return "Google search, YouTube, Android phones, and cloud services.";
            case "TSLA":
                return "Electric cars and clean energy. Led by Elon Musk.";
            case "AMZN":
                return "Online shopping, AWS cloud services, Prime Video.";
            case "MSFT":
                return "Windows, Office, Xbox, and cloud computing.";
            case "NFLX":
                return "Streaming movies and TV shows worldwide.";
            case "DIS":
                return "Disney movies, theme parks, Disney+, Marvel, Star Wars.";
            case "NKE":
                return "Athletic shoes and sportswear. 'Just Do It!'";
            case "SBUX":
                return "Coffee shops worldwide. That Frappuccino you love!";
            case "SPY":
                return "Owns pieces of 500 biggest US companies. Instant diversification!";
            case "QQQ":
                return "Owns the biggest tech companies on Nasdaq.";
            default:
                return "A publicly traded company you can own shares of!";
        }
    }
}