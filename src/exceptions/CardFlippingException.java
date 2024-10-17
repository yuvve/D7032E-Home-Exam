package exceptions;

/**
 * Exception thrown when a card cannot be flipped.
 */
public class CardFlippingException extends RuntimeException {
    public CardFlippingException(String message) {
        super(message);
    }
}
