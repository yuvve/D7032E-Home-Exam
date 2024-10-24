package exceptions;

/**
 * Exception thrown when deck generation fails.
 */
public class DeckGenerationException extends RuntimeException {
    public DeckGenerationException(String message) {
        super(message);
    }
}
