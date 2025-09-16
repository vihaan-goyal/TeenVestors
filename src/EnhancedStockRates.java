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
    
    // Get current stock price with fallback to estimated prices
    public static double getCurrentStockPrice(String symbol) {
        if (cachedRates.containsKey(symbol)) {
            return cachedRates.get(symbol);
        }
        
        // First try to get real data
        try {
            double realPrice = fetchRealStockPrice(symbol);
            if (realPrice > 0) {
                cachedRates.put(symbol, realPrice);
                return realPrice;
            }
        } catch (Exception e) {
            System.out.println("API call failed for " + symbol + ": " + e.getMessage());
        }
        
        // Fallback to estimated prices for educational purposes
        double estimatedPrice = getEstimatedPrice(symbol);
        cachedRates.put(symbol, estimatedPrice);
        return estimatedPrice;
    }
    
    private static double fetchRealStockPrice(String symbol) throws Exception {
        // Try Alpha Vantage API
        String apiKey = "OZNU3M6HUIMRQ25C";
        String urlStr = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        
        URI uri = URI.create(urlStr);
        URL url = uri.toURL();
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); // 5 second timeout
        conn.setReadTimeout(5000);
        
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP " + responseCode);
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        String json = response.toString();
        
        // Check for API limit message
        if (json.contains("API call frequency") || json.contains("premium endpoint")) {
            throw new Exception("API limit reached");
        }
        
        // Parse the current price from Global Quote
        String priceKey = "\"05. price\":\"";
        int startIndex = json.indexOf(priceKey);
        if (startIndex == -1) {
            // Try alternative parsing
            priceKey = "\"price\":\"";
            startIndex = json.indexOf(priceKey);
            if (startIndex == -1) throw new Exception("Price not found in response");
        }
        
        startIndex += priceKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        
        if (endIndex == -1) throw new Exception("Malformed price data");
        
        String priceStr = json.substring(startIndex, endIndex);
        return Double.parseDouble(priceStr);
    }
    
    // Fallback estimated prices for when API fails (educational purposes)
    private static double getEstimatedPrice(String symbol) {
        switch (symbol.toUpperCase()) {
            case "AAPL": return 175.00;
            case "GOOGL": return 2800.00;
            case "TSLA": return 250.00;
            case "AMZN": return 145.00;
            case "MSFT": return 420.00;
            case "NFLX": return 480.00;
            case "DIS": return 95.00;
            case "NKE": return 105.00;
            case "SBUX": return 100.00;
            case "SPY": return 450.00;
            case "QQQ": return 380.00;
            default: return 100.00;
        }
    }
    
    // Calculate potential stock investment growth (simplified model)
    public static double calculateStockGrowth(String symbol, double initialInvestment, int years) {
        try {
            double currentPrice = getCurrentStockPrice(symbol);
            if (currentPrice <= 0) {
                return initialInvestment; // Return original investment if no price
            }
            
            // Use historical average returns for different stock types
            double annualGrowthRate = getEstimatedGrowthRate(symbol);
            
            // Calculate shares that can be bought
            //double sharesBought = initialInvestment / currentPrice;
            
            // Apply compound growth with some volatility simulation
            double futureValue = initialInvestment;
            Random rand = new Random();
            
            for (int year = 1; year <= years; year++) {
                // Add some randomness to make it more realistic (+/- 20% of expected growth)
                double yearlyVariation = 1.0 + (rand.nextGaussian() * 0.1); // Normal distribution
                double thisYearGrowth = annualGrowthRate * yearlyVariation;
                futureValue *= (1 + thisYearGrowth);
            }
            
            // Ensure we don't go below 50% of original investment (crash protection for demo)
            if (futureValue < initialInvestment * 0.5) {
                futureValue = initialInvestment * 0.5;
            }
            
            return futureValue;
            
        } catch (Exception e) {
            System.out.println("Error calculating stock growth: " + e.getMessage());
            // Fallback calculation using simple compound interest
            double annualGrowthRate = getEstimatedGrowthRate(symbol);
            return initialInvestment * Math.pow(1 + annualGrowthRate, years);
        }
    }
    
    private static double getEstimatedGrowthRate(String symbol) {
        // Conservative growth rate estimates for educational purposes
        switch (symbol.toUpperCase()) {
            case "AAPL":
            case "GOOGL":
            case "MSFT":
                return 0.10; // 10% annual growth (established tech stocks)
            case "TSLA":
                return 0.12; // 12% (high growth but volatile)
            case "AMZN":
                return 0.11; // 11% (established growth)
            case "NFLX":
                return 0.08; // 8% (mature streaming)
            case "DIS":
            case "NKE":
            case "SBUX":
                return 0.07; // 7% (established consumer brands)
            case "SPY":
            case "QQQ":
                return 0.08; // 8% (historical market average)
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
            case "AMZN":
                return "MEDIUM-HIGH (Growth Stock)";
            case "NFLX":
                return "MEDIUM-HIGH (Streaming)";
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
                return "Apple makes iPhones, iPads, Macs, and Apple Watches. You probably use their products daily! They're known for premium design and innovation.";
            case "GOOGL":
                return "Google (Alphabet) runs Google search, YouTube, Gmail, Android phones, and cloud services. They make money mainly from advertising.";
            case "TSLA":
                return "Tesla makes electric cars, solar panels, and energy storage. Led by Elon Musk, they're pushing the world toward sustainable transport.";
            case "AMZN":
                return "Amazon started as online shopping but now does everything: AWS cloud services, Prime Video, Alexa, and even grocery stores.";
            case "MSFT":
                return "Microsoft makes Windows, Office (Word, Excel), Xbox gaming, and Azure cloud computing. Big in business software.";
            case "NFLX":
                return "Netflix streams movies and TV shows worldwide. They also create original content like Stranger Things and Wednesday.";
            case "DIS":
                return "Disney owns theme parks, Disney movies, Disney+, Marvel, Star Wars, Pixar, and ESPN. Entertainment empire!";
            case "NKE":
                return "Nike makes athletic shoes and sportswear worldwide. 'Just Do It!' They sponsor many famous athletes.";
            case "SBUX":
                return "Starbucks runs coffee shops worldwide. That Frappuccino you love! They're expanding globally, especially in Asia.";
            case "SPY":
                return "SPDR S&P 500 ETF owns tiny pieces of the 500 biggest US companies. Instant diversification in one purchase!";
            case "QQQ":
                return "Invesco QQQ ETF tracks the Nasdaq-100, focusing on the biggest tech companies. Higher growth potential than SPY.";
            default:
                return "A publicly traded company where you can buy shares and own a tiny piece of the business!";
        }
    }
    
    // Add a method to test if the API is working
    public static boolean testAPIConnection() {
        try {
            double testPrice = fetchRealStockPrice("AAPL");
            return testPrice > 0;
        } catch (Exception e) {
            return false;
        }
    }
}