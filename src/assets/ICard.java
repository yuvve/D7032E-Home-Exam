package assets;

import common.IRepresentable;
import exceptions.CardFlippingException;
import exceptions.CardFlippingException;

/**
 * Interface representing a card in the game, where one side is a scoring criteria and the other side is a resource.
 */
public interface ICard extends IRepresentable {

    /**
     * Gets the criteria strategy of the card.
     * @return The criteria strategy of the card.
     * @throws CardFlippingException If viewing the criteria side is forbidden due to game logic.
     */
    ICriteriaStrategy getCriteriaStrategy() throws CardFlippingException;

    /**
     * Gets the resource of the card.
     * @return The resource of the card.
     * @throws CardFlippingException If viewing the resource side is forbidden due to game logic.
     */
    IResource getResource() throws CardFlippingException;

    /**
     * Gets whether the criteria side of the card is active.
     * @return Whether the criteria side of the card is active.
     */
    boolean isCriteriaSideActive();

    /**
     * Gets whether the card can be flipped.
     * @return Whether the card can be flipped.
     */
    boolean canFlip();

    /**
     * Flips the card to the other side.
     * If the criteria side is active, the resource side becomes active, and vice versa.
     * @throws CardFlippingException If the card cannot be flipped due to game logic.
     */
    void flip() throws CardFlippingException ;
}
