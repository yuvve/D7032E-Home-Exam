package player;

import assets.ICard;
import common.IRepresentable;

import java.util.ArrayList;

/**
 * Interface for a game-domain player (not a multiplayer entity).
 */
public interface IPlayer extends IRepresentable {

    /**
     * Get the player's ID.
     * @return the player's ID
     */
    int getId();

    /**
     * Checks if the player is a bot.
     * @return true if the player is a bot, false otherwise
     */
    boolean isBot();

    /**
     * Make the player a bot.
     * Can be used to allow the match to continue if a player leaves.
     */
    void makeBot();

    /**
     * Get the player's hand.
     * @return the player's hand
     */
    ArrayList<ICard> getHand();

    /**
     * Gets all resource cards in the player's hand.
     * @return all resource cards in the player's hand
     */
    ArrayList<ICard> getResourceCards();

    /**
     * Gets all criteria cards in the player's hand.
     * @return all criteria cards in the player's hand
     */
    ArrayList<ICard> getCriteriaCards();

    /**
     * Discard a card from the player's hand.
     * @param card the card to discard
     */
    void discard(ICard card);

    /**
     * Add a card to the player's hand.
     * @param card the card to add
     */
    void addToHand(ICard card);

}
