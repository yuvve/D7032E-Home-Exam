package exceptions;

/**
 * Exception thrown when trying to add a card to a market position that is already occupied.
 */
public class MarketPositionOccupied extends RuntimeException {
    public MarketPositionOccupied(String message) {
        super(message);
    }
}
