import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Rates {

    private static Double cachedRate = null;

    public static double getSP500Rate() {
        if (cachedRate == null) {
            cachedRate = fetchSP500RateFromAPI();
        }
        return cachedRate;
    }

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

            String json = response.toString();

            // Extract "Time Series (Daily)" object manually
            String timeSeriesKey = "\"Time Series (Daily)\":{";
            int startIndex = json.indexOf(timeSeriesKey);
            if (startIndex == -1) throw new Exception("No Time Series found");
            startIndex += timeSeriesKey.length();

            // Find end of Time Series object (assumes balanced braces)
            int braceCount = 1;
            int endIndex = startIndex;
            while (braceCount > 0 && endIndex < json.length()) {
                char c = json.charAt(endIndex);
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                endIndex++;
            }
            if (braceCount != 0) throw new Exception("Malformed JSON");

            String timeSeriesJson = json.substring(startIndex, endIndex - 1);

            // Extract latest date key (first key in timeSeriesJson)
            int dateStart = timeSeriesJson.indexOf("\"") + 1;
            int dateEnd = timeSeriesJson.indexOf("\"", dateStart);
            if (dateStart == 0 || dateEnd == -1) throw new Exception("No date found");

            String latestDate = timeSeriesJson.substring(dateStart, dateEnd);

            // Extract the block for latestDate
            String dateKey = "\"" + latestDate + "\":{";
            int latestDataStart = timeSeriesJson.indexOf(dateKey);
            if (latestDataStart == -1) throw new Exception("No data for latest date");
            latestDataStart += dateKey.length();

            // Find end of latest date data
            braceCount = 1;
            int latestDataEnd = latestDataStart;
            while (braceCount > 0 && latestDataEnd < timeSeriesJson.length()) {
                char c = timeSeriesJson.charAt(latestDataEnd);
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                latestDataEnd++;
            }
            if (braceCount != 0) throw new Exception("Malformed JSON in latest data");

            String latestDataJson = timeSeriesJson.substring(latestDataStart, latestDataEnd -1);

            // Helper to extract value by key inside latestDataJson
            double open = extractDouble(latestDataJson, "\"1. open\"");
            double close = extractDouble(latestDataJson, "\"4. close\"");

            double dailyChange = (close - open) / open;
            double annualizedReturn = dailyChange * 250; // Approx 250 trading days/year

            return annualizedReturn;

        } catch (Exception e) {
            System.out.println("Error fetching S&P 500 rate: " + e.getMessage());
            return 0.08; // fallback average return
        }
    }

    private static double extractDouble(String json, String key) throws Exception {
        int keyIndex = json.indexOf(key);
        if (keyIndex == -1) throw new Exception("Key " + key + " not found");
        int colonIndex = json.indexOf(":", keyIndex);
        int commaIndex = json.indexOf(",", colonIndex);
        int endIndex = commaIndex != -1 ? commaIndex : json.length();

        String valueStr = json.substring(colonIndex + 1, endIndex).trim();

        // Remove quotes if present
        if (valueStr.startsWith("\"") && valueStr.endsWith("\"")) {
            valueStr = valueStr.substring(1, valueStr.length() -1);
        }

        return Double.parseDouble(valueStr);
    }
}
