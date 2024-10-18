package assets;

import common.IRepresentable;

import java.util.ArrayList;

/**
 * Interface for a pile of cards in the game.
 */
public interface IPile extends IRepresentable {

    /**
     * Shuffles the pile.
     */
    void shuffle();

    /**
     * Gets the number of cards in the pile.
     * @return The number of cards.
     */
    int getCardCount();

    /**
     * Views the top card from the pile.
     * Will return null if the pile is empty.
     * @return The top card.
     */
    ICard viewTop();

    /**
     * Draws (fetch and remove) the top card from the pile.
     * Will return null if the pile is empty.
     * @return The top card.
     */
    ICard drawTop();

    /**
     * Draws (fetch and remove) the bottom card from the pile.
     * Will return null if the pile is empty.
     * @return The bottom card.
     */
    ICard drawBottom();
}
