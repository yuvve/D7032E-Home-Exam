package exceptions;

/**
 * Exception thrown when a card cannot be generated.
 */
public class CardGenerationException extends RuntimeException {
    public CardGenerationException(String message) {
        super(message);
    }
}
