package A2;

import java.io.*;
import java.util.ArrayList;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Class that cleans and transforms data stored in files and stored it in mongoDB database.
 *
 */

public class DataTransformation  {

    /**
     * Method that cleans data stored in files by removing urls, non ASCII characters and emoticons.
     *
     * @param response file that contains title and content of the article
     */

    public void dataCleaner(File response) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(response));
        ArrayList<String> articleInfo = new ArrayList<>();
        articleInfo.add(reader.readLine());
        articleInfo.add(reader.readLine());
        ArrayList<String> modifiedLines = new ArrayList<>();
        for (int i = 0; i < articleInfo.size(); i++) {
            String currentLine = articleInfo.get(i);
            //Adapted from https://stackoverflow.com/
            //Adapted from https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/regex/Pattern.html
            currentLine = currentLine.replaceAll("(?i)\\b(?:https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", ""); // remove URLs
            currentLine = currentLine.replaceAll("[^\\x00-\\x7F]", ""); // remove non-ASCII characters
            currentLine = currentLine.replaceAll(":\"(\\\\u[a-f0-9]{4}|\\\\[^u]|[^\\\\\"])*\"", ":\"\""); // remove emoticons
            currentLine = currentLine.replaceAll("[^a-zA-Z0-9\\s]+", ""); // remove special characters
            modifiedLines.add(currentLine);
        }
        articleInfo = modifiedLines;
        writeArticle(articleInfo);
        reader.close();
    }

    /**
     * Method to arrange data before storing in database.
     *
     * @param currentArticle transformed title and content of current article.
     *
     */

    public boolean writeArticle(ArrayList<String> currentArticle){
        ArrayList<String> keys = new ArrayList<>();
        keys.add("title");
        keys.add("content");
        ArrayList<String> values = new ArrayList<>();
        for(String articleInfo: currentArticle){
            values.add(articleInfo);
        }
        if(writeToMongoDB(keys, values) != true){
            return false;
        }
        return true;
    }

    /**
     * Method to store title and content as a document in mongoDB collection
     *
     * @param keys, values: data to be stored in document.
     * References:
     * [1] Brightspace: CSCI5408 - Lab6 https://dal.brightspace.com/d2l/le/content/248902/Home?itemIdentifier=D2L.LE.Content.ContentObject.ModuleCO-3547176
     *
     */

    public boolean writeToMongoDB(ArrayList<String> keys, ArrayList<String> values) {
        try{
            // Initialize variables
            MongoDatabase mongoDatabase = null;
            MongoClientSettings settings = null;
            MongoClient mongoClient = null;
            MongoCollection mongoCollection = null;

            // MongoDB connection link with personal information
            ConnectionString connectionString = new ConnectionString("mongodb+srv://mudraverma65:L2C67twe18oJF11i@cluster0.qqfayg3.mongodb.net/?retryWrites=true&w=majority");
            settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
            mongoDatabase = mongoClient.getDatabase("NewsArticles");
            Document document = new Document("_id", new ObjectId());

            // Create JSON like document with unique ID
            for(int i=0; i<2; i++){
                document.append(keys.get(i),values.get(i) );
            }

            // Insert Data into Database with creating new collection
            mongoCollection = mongoDatabase.getCollection("Articles");
            mongoCollection.insertOne(document);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

}
