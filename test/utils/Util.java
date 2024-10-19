package utils;

import org.json.JSONObject;
import point_salad.AssetsTests;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Utility class for testing
 */
public class Util {

    /**
     * Converts a file to a JSON object
     * @param filename the name of the file
     * @return the JSON object
     * @throws FileNotFoundException if the file is not found
     */
    public static JSONObject fileToJSON(String filename) throws FileNotFoundException {
        try (InputStream inputStream = AssetsTests.class.getClassLoader().getResourceAsStream(filename)){
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filename);
            }
            String jsonText = new String(inputStream.readAllBytes());
            return new JSONObject(jsonText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
