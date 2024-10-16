package assets;

import common.IRepresentable;

/**
 * Interface representing a card in the game, where one side is a scoring criteria and the other side is a resource.
 */
public interface ICard extends IRepresentable {

    /**
     * Gets the criteria strategy of the card.
     * @return The criteria strategy of the card.
     */
    ICriteriaStrategy getCriteriaStrategy();

    /**
     * Gets the resource of the card.
     * @return The resource of the card.
     */
    IResource getResource();

    /**
     * Gets whether the criteria side of the card is active.
     * @return Whether the criteria side of the card is active.
     */
    boolean isCriteriaSideActive();

    /**
     * Flips the card to the other side.
     * If the criteria side is active, the resource side becomes active, and vice versa.
     */
    void flipCriteriaSide();
}
