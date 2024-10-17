package assets;

import common.IRepresentable;
import exceptions.MarketCardPlacementException;

/**
 * Interface for the card market.
 * Contains the cards that are available for drafting.
 */
public interface IMarket extends IRepresentable {

    /**
     * Gets the cards in the market.
     * @return The cards in the market.
     */
    ICard[][] viewCards();

    /**
     * Draws a card from the market from the given coordinates.
     * @param row The row of the card.
     *            Must be between 0 and the number of rows in the market cards.
     * @param column The column of the card.
     *               Must be between 0 and the number of columns in the market cards.
     * Will return null if the slot is empty.
     * @return The card drawn.
     */
    ICard draftCard(int row, int column);

    /**
     * Place a card in the market at the given coordinates.
     * @param row The row of the card.
     *            Must be between 0 and the number of rows in the market cards.
     * @param column The column of the card.
     *               Must be between 0 and the number of columns in the market cards.
     * @throws MarketCardPlacementException If the position is already occupied.
     */
    void placeCardInPosition(ICard card, int row, int column) throws MarketCardPlacementException;
}
