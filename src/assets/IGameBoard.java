package assets;

import common.IRepresentable;
import exceptions.MarketPlacementForbiddenDueToGameLogic;
import exceptions.MarketPositionOccupied;

import java.util.ArrayList;

/**
 * Interface for what is considered in the game-domain a game board.
 * Contains the card pile(s) and the market cards.
 */
public interface IGameBoard extends IRepresentable {
    /**
     * Gets the card piles.
     * @return The card piles.
     */
    ArrayList<IPile> getPiles();

    /**
     * Gets the market cards.
     * @return The market cards.
     */
    ICard[][] getMarketCards();

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
     * @throws MarketPositionOccupied If the position is already occupied.
     * @throws MarketPlacementForbiddenDueToGameLogic If the placement is forbidden due to game rules.
     */
    void placeCardInMarket(ICard card, int row, int column)
            throws MarketPositionOccupied, MarketPlacementForbiddenDueToGameLogic;
}
