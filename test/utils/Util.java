package utils;

import org.json.JSONObject;
import point_salad.AssetsTests;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Util {
    private final String filename = "file.json";

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
