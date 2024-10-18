package assets;

import exceptions.DeckGenerationException;
import exceptions.PileGenerationException;
import exceptions.MarketGenerationException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Interface for an abstract factory that generates assets according to game rules.
 */
public interface IAbstractAssetsFactory {

    /**
     * Creates a game board.
     * @param deckJson The JSON object representing the deck of cards.
     * @param numPlayers The number of players.
     * @return The game board.
     * @throws PileGenerationException Thrown if the deck is incorrect for pile generation.
     * @throws MarketGenerationException Thrown if the piles are incorrect for market generation.
     */
    IGameBoard createGameBoard(JSONObject deckJson, int numPlayers)
            throws PileGenerationException, MarketGenerationException;

    /**
     * Creates a deck of cards.
     * @param deckJson The JSON object representing the deck of cards.
     * @param numPlayers The number of players.
     * @return The deck of cards.
     */
    ArrayList<ICard> createDeck(JSONObject deckJson, int numPlayers)
            throws DeckGenerationException;

    /**
     * Creates piles.
     * @param deck The deck of cards.
     * @return The piles.
     * @throws PileGenerationException Thrown if any deck is incorrect for pile generation.
     */
    ArrayList<IPile> createPiles(ArrayList<ICard> deck) throws PileGenerationException;

    /**
     * Creates a market.
     * @param piles The piles of cards.
     * @return The market.
     * @throws MarketGenerationException Thrown if the piles are incorrect for market generation.
     */
    IMarket createMarket(ArrayList<IPile> piles) throws MarketGenerationException;

    /**
     * Creates a pile.
     * @param cards The cards in the pile.
     * @return The pile.
     */
    IPile createPile(ArrayList<ICard> cards);

    /**
     * Creates a card.
     * @param cardJson The JSON object representing the card.
     * @return The card.
     */
    ICard createCard(JSONObject cardJson);

    /**
     * Creates a criteria strategy.
     * @param criteriaJson The JSON object representing the criteria strategy.
     * @return The criteria strategy.
     */
    ICriteriaStrategy createCriteria(JSONObject criteriaJson);

    /**
     * Creates a resource.
     * @param resourceName The name of the resource.
     * @return The resource.
     */
    IResource createResource(String resourceName);
}
