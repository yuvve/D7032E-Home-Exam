package game;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Util {

    /**
     * Gets the IP address and port from the user
     * @return the IP address and port
     */
    public static String getIpPort(Scanner scanner){
        String ipPort = scanner.nextLine();
        do {
            if (!validateIpPort(ipPort)) {
                System.out.println("Invalid input. Please enter an IP address and port in the form [IP]:[PORT], " +
                        "or enter nothing to host a server.");
                ipPort = scanner.nextLine();
            }
        } while (!validateIpPort(ipPort));
        return ipPort;
    }

    /**
     * Validates that user input is an IP address and port
     * @param input the user's input
     * @return true if:
     *  The input is in the form [IP]:[PORT] and PORT is an integer between 0 and 65535
     *  OR the input is empty
     * false otherwise
     */
    public static boolean validateIpPort(String input){
        if (input.isEmpty()) return true;
        String[] parts = input.split(":");
        String portString;
        if (parts.length == 1)
        {
            portString = parts[0];
        }
        else if (parts.length == 2) {
            portString = parts[1];
        }
        else{
            return false;
        }
        try {
            int port = Integer.parseInt(portString);
            return port >= 0 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates that user input is one character from an array of valid inputs
     * @param validInputs the valid inputs
     * @param caseSensitive whether the input is case-sensitive
     * @return true if the input is valid, false otherwise
     */
    public static boolean validateInput(String input, char[] validInputs, boolean caseSensitive) {
        if (!caseSensitive) {
            for (int i = 0; i < validInputs.length; i++) {
                validInputs[i] = Character.toLowerCase(validInputs[i]);
            }
        }
        if (input.length() == 1) {
            char c = input.charAt(0);
            for (char validInput : validInputs) {
                if (!caseSensitive) c = Character.toLowerCase(c);
                if (c == validInput) return true;
            }
        }
        return false;
    }

    /**
     * Gets an integer between a minimum and maximum values from the user
     * @param min the minimum value
     * @param max the maximum value
     * @return the valid input
     */
    public static int getValidInput(Scanner scanner, int min, int max) {
        String input = scanner.nextLine();
        do {
            if (!validateInput(input, min, max)) {
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                input = scanner.nextLine();
            }
        } while (!validateInput(input, min, max));
        return Integer.parseInt(input);
    }

    /**
     * Validates that user input is an integer between a minimum and maximum values
     * @param min the minimum value
     * @param max the maximum value
     * @return true if the input is valid, false otherwise
     */
    public static boolean validateInput(String input, int min, int max) {
        try {
            int i = Integer.parseInt(input);
            if (i >= min && i <= max) return true;
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    /**
     * Converts a file to a JSON object
     * @param filename the name of the file
     * @return the JSON object
     * @throws FileNotFoundException if the file is not found
     */
    public static JSONObject fileToJSON(String filename) throws FileNotFoundException {
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filename)){
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
