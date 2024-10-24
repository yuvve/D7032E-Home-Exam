package exceptions;

/**
 * Exception thrown when a card cannot be discarded.
 */
public class CardDiscardingException extends RuntimeException {
    public CardDiscardingException(String message) {
        super(message);
    }
}
