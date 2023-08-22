package A2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This class extracts news articles from News API.
 *
 * References:
 * [1] NewsAPI: https://newsapi.org/
 * [2] DigitalOcean: https://www.digitalocean.com/community/tutorials/java-httpurlconnection-example-java-http-request-get-post
 */

public class DataExtraction {
    DataProcessing dataProcessing = new DataProcessing();
    public void Extract() throws Exception {
        //Get a list of keywords
        Keywords k1 = new Keywords();
        ArrayList<String> keywordsString = k1.createKeywords();
        for (String query: keywordsString){
            URL url = new URL("https://newsapi.org/v2/top-headlines?q="+ query +"&apiKey=5bdc2ad306a842938b38d5a8811d6c1f");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Read the response line by line and append to a string builder
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String res = response.toString();
                //Call method to process data
                dataProcessing.dataProcessingEngine(res);
            } else {
                System.out.println("Error response: " + connection.getResponseMessage());
            }
            connection.disconnect();
        }
    }
}
