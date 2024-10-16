package exceptions;

/**
 * Exception thrown when a market placement is forbidden due to game logic.
 */
public class MarketPlacementForbiddenDueToGameLogic extends RuntimeException {
    public MarketPlacementForbiddenDueToGameLogic(String message) {
        super(message);
    }
}
