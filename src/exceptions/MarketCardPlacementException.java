package exceptions;

/**
 * Exception for when a card cannot be placed in the market.
 */
public class MarketCardPlacementException extends RuntimeException {
    public MarketCardPlacementException(String message) {
        super(message);
    }
}
