package src;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.json.JSONObject;

public class Rates {

    // Stores the cached rate once it's retrieved
    private static Double cachedRate = null;

    
    // Public method that returns either cached or freshly fetched rate
    public static double getSP500Rate() {
        if (cachedRate == null) {
            cachedRate = fetchSP500RateFromAPI();
        }
        return cachedRate;
    }

    // Internal method that does the actual API call and parsing
    private static double fetchSP500RateFromAPI() {
        try {
            String apiKey = "OZNU3M6HUIMRQ25C";
            String urlStr = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=SPY&apikey=" + apiKey;


            URI uri = URI.create(urlStr);
            URL url = uri.toURL(); 

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONObject timeSeries = json.getJSONObject("Time Series (Daily)");

            Iterator<String> keys = timeSeries.keys();
            String latestDate = keys.hasNext() ? keys.next() : null;

            if (latestDate == null) throw new Exception("No date found in response.");

            JSONObject latestData = timeSeries.getJSONObject(latestDate);

            double open = Double.parseDouble(latestData.getString("1. open"));
            double close = Double.parseDouble(latestData.getString("4. close"));

            double dailyChange = (close - open) / open;
            double annualizedReturn = dailyChange * 250; // Approx 250 trading days/year

            return annualizedReturn;

        } catch (Exception e) {
            System.out.println("Error fetching S&P 500 rate: " + e.getMessage());
            return 0.08; // fallback average return
        }
    }
}
