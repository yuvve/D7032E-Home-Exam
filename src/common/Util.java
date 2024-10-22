package common;

/**
 * Utility class for common module
 */
public class Util {
    /**
     * Flushes the system input stream to remove any input the user typed before their turn.
     */
    public static void flushSystemIn(){
        try {
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception IOException) {
            System.err.println("Error: Could not flush input stream.");
        }
    }
}
