package assets;

import common.IRepresentable;

import java.util.ArrayList;

/**
 * Interface for what is considered in the game-domain a game board.
 * Contains the card pile(s) and the market.
 */
public interface IGameBoard extends IRepresentable {

    /**
     * Gets a specific pile of cards.
     * @param index The index of the pile.
     * @return The pile of cards.
     */
    IPile getPile(int index) throws IllegalArgumentException;

    /**
     * Returns the indexes of all non-empty piles.
     * @return The indexes of all non-empty piles.
     */
    ArrayList<Integer> getNonEmptyPiles();

    /**
     * Gets the market.
     * @return The market.
     */
    IMarket getMarket();

    /**
     * Checks if the game has ended.
     * @return True if the game has ended, false otherwise.
     */
    boolean hasGameEnded();

}
