package assets;

import common.IRepresentable;

import java.util.ArrayList;

/**
 * Interface for what is considered in the game-domain a game board.
 * Contains the card pile(s) and the market.
 */
public interface IGameBoard extends IRepresentable {


    /** Gets a pile of cards from the game board.
     * @param pileIndex The index of the pile.
     *                  Must be between 0 and the number of piles in the game board.
     * @return The pile of cards.
     */
    ICard getCardFromPile(int pileIndex) throws IllegalArgumentException;

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
     * Refills the market.
     */
    void refillMarket();

    /**
     * Checks if the game has ended.
     * @return True if the game has ended, false otherwise.
     */
    boolean hasGameEnded();

}
