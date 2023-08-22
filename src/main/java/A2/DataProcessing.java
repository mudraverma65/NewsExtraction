package A2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Process extracted data and store it in files
 *
 */

public class DataProcessing {

    DataTransformation dataTransform = new DataTransformation();

    /**
     * Processes the given response string and writes the extracted article data to a file,
     * which is then passed to the data cleaner for further processing.
     *
     * @param response the JSON response string containing news article data
     */

    public void dataProcessingEngine(String response) throws IOException {
        //File object creation
        File file = new File("response.txt");

        //Regex pattern to match the article data in the json response and store tile and content
        //Adapted from https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/regex/Pattern.html
        Pattern articlePattern = Pattern.compile("\\{\"source\":\\{\"id\":\"[^\"]*\",\"name\":\"[^\"]*\"\\},\"author\":\"?[^\"]*\"?,\"title\":\"([^\"]*)\",\"description\":\"?[^\"]*\"?,\"url\":\"?[^\"]*\"?,\"urlToImage\":\"?[^\"]*\"?,\"publishedAt\":\"?[^\"]*\"?,\"content\":\"?([^\"]*)\"?\\}");

        //Find article data
        Matcher articleMatcher = articlePattern.matcher(response);

        //loop through all matches in the string
        while (articleMatcher.find()) {
            String articleJson = articleMatcher.group(1) + "\n" + articleMatcher.group(2);
            try {
                FileWriter fileWriter = new FileWriter(file.getName());

                //write article data to file
                fileWriter.write(articleJson);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error writing article to file: " + e.getMessage());
            }
            dataTransform.dataCleaner(file);
        }
    }
}
