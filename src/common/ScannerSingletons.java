package common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Gets the Scanner singleton for a given input stream (only one scanner per stream is allowed).
 */
public class ScannerSingletons {
    private static HashMap<InputStream, Scanner> instances = new HashMap<>();

    private ScannerSingletons() {}

    public static Scanner getInstance(InputStream stream) {
        if (!instances.containsKey(stream)) {
            instances.put(stream, new Scanner(stream));
        }
        return instances.get(stream);
    }

    public static void close(InputStream stream) {
        if (instances.containsKey(stream)) {
            instances.get(stream).close();
            instances.remove(stream);
        }
    }

    public static void closeAll() {
        for (Scanner scanner : instances.values()) {
            scanner.close();
        }
        instances.clear();
    }
}