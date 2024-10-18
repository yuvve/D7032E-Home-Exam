package exceptions;

/**
 * Exception thrown when market generation fails.
 */
public class MarketGenerationException extends RuntimeException {
    public MarketGenerationException(String message) {
        super(message);
    }
}
