package exceptions;

/**
 * Exception thrown when card flipping is forbidden due to game logic.
 */
public class CardFlippingForbiddenDueToGameLogic extends RuntimeException {
    public CardFlippingForbiddenDueToGameLogic(String message) {
        super(message);
    }
}
