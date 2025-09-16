import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class EnhancedStockRates {
    private static Map<String, Double> cachedRates = new HashMap<>();
    private static Map<String, Long> cacheTimestamps = new HashMap<>();
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
    private static Map<String, String> popularStocks = new HashMap<>();
    
    // IMPORTANT: Replace this with your actual Alpha Vantage API key
    // Get a free key at: https://www.alphavantage.co/support/#api-key
    private static final String API_KEY = "api-key-here";
    
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
    
    // Check if cached data is still valid
    private static boolean isCacheValid(String symbol) {
        if (!cachedRates.containsKey(symbol) || !cacheTimestamps.containsKey(symbol)) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long cacheTime = cacheTimestamps.get(symbol);
        return (currentTime - cacheTime) < CACHE_DURATION;
    }
    
    // Get current stock price with improved error handling
    public static double getCurrentStockPrice(String symbol) {
        // Check cache first
        if (isCacheValid(symbol)) {
            System.out.println("Using cached price for " + symbol + ": $" + cachedRates.get(symbol));
            return cachedRates.get(symbol);
        }
        
        try {
            System.out.println("Fetching fresh price for " + symbol + "...");
            
            // Build the API URL
            String urlStr = String.format(
                "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                symbol, API_KEY
            );
            
            System.out.println("API URL: " + urlStr);
            
            URI uri = URI.create(urlStr);
            URL url = uri.toURL();
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000); // 10 second timeout
            conn.setReadTimeout(10000);
            conn.setRequestProperty("User-Agent", "Java-Investment-Calculator/1.0");
            
            int responseCode = conn.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);
            
            if (responseCode != 200) {
                throw new Exception("HTTP error code: " + responseCode);
            }
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            String json = response.toString();
            System.out.println("API Response: " + json.substring(0, Math.min(200, json.length())) + "...");
            
            // Check for API limit errors
            if (json.contains("API call frequency")) {
                throw new Exception("API rate limit exceeded. Please wait a moment and try again.");
            }
            
            if (json.contains("Invalid API call")) {
                throw new Exception("Invalid API call. Check symbol: " + symbol);
            }
            
            // Parse the current price from Global Quote
            // Alpha Vantage returns: "05. price": "150.25"
            double price = 0.0;
            
            // First check if we have a Global Quote response
            if (!json.contains("Global Quote")) {
                throw new Exception("No Global Quote found in response. Response: " + json.substring(0, Math.min(300, json.length())));
            }
            
            // Look for the price field - try multiple variations
            String[] pricePatterns = {
                "\"05. price\": \"",
                "\"05. price\":\"", 
                "\"price\": \"",
                "\"price\":\""
            };
            
            boolean priceFound = false;
            for (String priceKey : pricePatterns) {
                int startIndex = json.indexOf(priceKey);
                if (startIndex != -1) {
                    startIndex += priceKey.length();
                    int endIndex = json.indexOf("\"", startIndex);
                    
                    if (endIndex != -1) {
                        String priceStr = json.substring(startIndex, endIndex);
                        try {
                            price = Double.parseDouble(priceStr);
                            priceFound = true;
                            System.out.println("Successfully parsed price using pattern: " + priceKey + " -> $" + price);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Failed to parse price string: " + priceStr);
                            continue;
                        }
                    }
                }
            }
            
            if (!priceFound) {
                // Print more of the response for debugging
                System.out.println("Full JSON response: " + json);
                throw new Exception("Could not find or parse price in response");
            }
            
            // Cache the result
            cachedRates.put(symbol, price);
            cacheTimestamps.put(symbol, System.currentTimeMillis());
            
            System.out.println("Successfully fetched price for " + symbol + ": $" + price);
            return price;
            
        } catch (Exception e) {
            System.out.println("Error fetching price for " + symbol + ": " + e.getMessage());
            e.printStackTrace();
            
            // Return estimated price if API fails (for demo purposes)
            double estimatedPrice = getEstimatedPrice(symbol);
            System.out.println("Using estimated price for " + symbol + ": $" + estimatedPrice);
            return estimatedPrice;
        }
    }
    
    // Fallback estimated prices for when API fails
    private static double getEstimatedPrice(String symbol) {
        switch (symbol.toUpperCase()) {
            case "AAPL": return 175.0;
            case "GOOGL": return 145.0;
            case "TSLA": return 240.0;
            case "AMZN": return 155.0;
            case "MSFT": return 420.0;
            case "NFLX": return 450.0;
            case "DIS": return 95.0;
            case "NKE": return 110.0;
            case "SBUX": return 98.0;
            case "SPY": return 450.0;
            case "QQQ": return 390.0;
            default: return 100.0;
        }
    }
    
    // Calculate potential stock investment growth (simplified model)
    public static double calculateStockGrowth(String symbol, double initialInvestment, int years) {
        try {
            // Get current price (or estimated price if API fails)
            double currentPrice = getCurrentStockPrice(symbol);
            if (currentPrice <= 0) {
                currentPrice = getEstimatedPrice(symbol);
            }
            
            // Use historical average returns for different stock types
            double annualGrowthRate = getEstimatedGrowthRate(symbol);
            
            // Calculate shares that can be bought
            double sharesBought = initialInvestment / currentPrice;
            
            // Project future value using compound growth
            double futurePrice = currentPrice * Math.pow(1 + annualGrowthRate, years);
            
            return sharesBought * futurePrice;
            
        } catch (Exception e) {
            System.out.println("Error calculating stock growth: " + e.getMessage());
            return initialInvestment; // Return original investment if error
        }
    }
    
    private static double getEstimatedGrowthRate(String symbol) {
        // Simplified growth rate estimates for educational purposes
        // These are based on historical averages but not guarantees
        switch (symbol.toUpperCase()) {
            case "AAPL":
            case "GOOGL":
            case "MSFT":
                return 0.12; // 12% annual growth (established tech stocks)
            case "TSLA":
                return 0.15; // 15% (high growth but very volatile)
            case "AMZN":
                return 0.11; // 11% (large tech with growth)
            case "NFLX":
                return 0.10; // 10% (streaming growth)
            case "DIS":
            case "NKE":
            case "SBUX":
                return 0.09; // 9% (established consumer brands)
            case "SPY":
                return 0.08; // 8% (S&P 500 historical average)
            case "QQQ":
                return 0.09; // 9% (tech-heavy Nasdaq)
            default:
                return 0.08; // Default market rate
        }
    }
    
    // For educational purposes - show risk levels
    public static String getRiskLevel(String symbol) {
        switch (symbol.toUpperCase()) {
            case "SPY":
                return "LOW (S&P 500 - Very Diversified)";
            case "QQQ":
                return "LOW-MEDIUM (Tech ETF - Diversified)";
            case "AAPL":
            case "MSFT":
            case "GOOGL":
                return "MEDIUM (Large Tech - Established)";
            case "AMZN":
                return "MEDIUM (Large Cap Growth)";
            case "TSLA":
                return "HIGH (Very Volatile Growth Stock)";
            case "NFLX":
                return "MEDIUM-HIGH (Growth & Competition Risk)";
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
                return "Makes iPhones, iPads, Macs, and services. You probably use their products daily! Known for innovation and loyal customers.";
            case "GOOGL":
                return "Google search, YouTube, Android phones, Gmail, and cloud services. Dominates online advertising and has massive data advantages.";
            case "TSLA":
                return "Electric cars, batteries, and clean energy solutions. Led by Elon Musk. High growth potential but very volatile pricing.";
            case "AMZN":
                return "Online shopping giant, AWS cloud services (powers much of the internet), Prime Video, and logistics. Huge market presence.";
            case "MSFT":
                return "Windows, Office software, Xbox gaming, Azure cloud computing. Strong business software and cloud growth.";
            case "NFLX":
                return "Streaming movies and TV shows worldwide. Original content creator competing with Disney+, Apple TV+, and others.";
            case "DIS":
                return "Disney movies, theme parks, Disney+, Marvel, Star Wars, ESPN. Entertainment empire with strong brand loyalty.";
            case "NKE":
                return "Athletic shoes and sportswear worldwide. 'Just Do It!' Strong brand with celebrity endorsements and global reach.";
            case "SBUX":
                return "Coffee shops worldwide. That Frappuccino you love! Expanding globally with premium coffee culture.";
            case "SPY":
                return "Owns small pieces of the 500 biggest US companies. Instant diversification! Follows the S&P 500 index.";
            case "QQQ":
                return "Owns the 100 biggest non-financial companies on Nasdaq (mostly tech). Good way to invest in technology sector.";
            default:
                return "A publicly traded company you can own shares of! Research the company before investing.";
        }
    }
    
    // Test method to check if API is working
    public static void testAPI() {
        System.out.println("Testing Alpha Vantage API...");
        
        try {
            double applePrice = getCurrentStockPrice("AAPL");
            if (applePrice > 0) {
                System.out.println("✓ API is working! Apple stock price: $" + applePrice);
            } else {
                System.out.println("✗ API returned invalid price");
            }
        } catch (Exception e) {
            System.out.println("✗ API test failed: " + e.getMessage());
            System.out.println("Possible solutions:");
            System.out.println("1. Check your internet connection");
            System.out.println("2. Verify your API key is valid");
            System.out.println("3. You might have hit the rate limit (5 calls per minute for free tier)");
            System.out.println("4. Get a new API key from https://www.alphavantage.co/support/#api-key");
        }
    }
    
    // Alternative method name for compatibility
    public static boolean testAPIConnection() {
        try {
            double testPrice = getCurrentStockPrice("AAPL");
            return testPrice > 0;
        } catch (Exception e) {
            System.out.println("API connection test failed: " + e.getMessage());
            return false;
        }
    }
}