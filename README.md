News Extraction System.

This is a Java application that performs keyword related search on a news API.  Once the articles are fetched the title and content of the article in stored in MongoDB database.

### Classes:
1. DataExtraction: Extracts data from NewsAPI.
2. DataProcessing: Initiated after Data Extraction, this class writes the news content and titles to files. Each file stores only one article at a time.
3. DataTransformation: Automatically cleans and transforms the data stored in file and uploads it to the MongoDB database as a new collection.

News API used: https://newsapi.org/

This was developed as Assignment 2 for CSCI5408.