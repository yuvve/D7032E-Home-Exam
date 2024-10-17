package exceptions;

/**
 * Exception thrown when pile generation fails.
 */
public class PileGenerationException extends RuntimeException {
    public PileGenerationException(String message) {
        super(message);
    }
}
