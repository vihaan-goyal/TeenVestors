package src;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;


public class Rates {
    public static double getSP500RateFromAPI() {
        try {
            String apiKey = "OZNU3M6HUIMRQ25C";
            String urlStr = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=^GSPC&apikey=" + apiKey;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            // Parse JSON (you'll need org.json for this line OR write a simple parser)
            JSONObject json = new JSONObject(response.toString());
            JSONObject timeSeries = json.getJSONObject("Time Series (Daily)");

            String latestDate = timeSeries.keys().next();
            JSONObject latestData = timeSeries.getJSONObject(latestDate);

            double open = Double.parseDouble(latestData.getString("1. open"));
            double close = Double.parseDouble(latestData.getString("4. close"));

            double percentChange = ((close - open) / open) * 100;

            // Estimate annualized return
            return (percentChange / 1.0) * 250 / 100.0;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0.08; // fallback
        }
    }
}

