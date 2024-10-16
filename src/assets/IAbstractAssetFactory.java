package assets;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Interface for an abstract factory that generates assets.
 */
public interface IAbstractAssetFactory {
    /**
     * Generates a deck of cards from a JSON object.
     * @param deckJson The JSON object representing the deck.
     * @return The deck of cards.
     */
    ArrayList<ICard> generateDeck(JSONObject deckJson);

    /**
     * Creates a card from a JSON object.
     * @param cardJson The JSON object representing the card.
     * @return The card.
     */
    ICard createCard(JSONObject cardJson);

    /**
     * Creates a criteria strategy from a JSON object.
     * @param criteriaStrategyJson The JSON object representing the criteria strategy.
     * @return The criteria strategy.
     */
    ICriteriaStrategy createCriteria(JSONObject criteriaStrategyJson);

    /**
     * Creates a resource from a JSON object.
     * @param resourceJson The JSON object representing the resource (must include the name of a resource enum).
     * @return The resource.
     */
    IResource createResource(JSONObject resourceJson);
}
