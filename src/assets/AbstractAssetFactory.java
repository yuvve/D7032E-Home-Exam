package assets;

import exceptions.DeckGenerationException;
import exceptions.PileGenerationException;
import exceptions.MarketGenerationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Interface for an abstract factory that generates assets according to game rules.
 */
public abstract class AbstractAssetFactory implements IGameBoardFactory {

    @Override
    public abstract IGameBoard createGameBoard(String deckRawString, int numPlayers)
            throws PileGenerationException, MarketGenerationException, JSONException;

    /**
     * Creates a deck of cards.
     * @param deckJson The JSON object representing the deck of cards.
     * @param numPlayers The number of players.
     * @return The deck of cards.
     */
    protected abstract ArrayList<ICard> createDeck(JSONObject deckJson, int numPlayers)
            throws DeckGenerationException;

    /**
     * Creates piles.
     * @param deck The deck of cards.
     * @return The piles.
     * @throws PileGenerationException Thrown if any deck is incorrect for pile generation.
     */
    protected abstract ArrayList<IPile> createPiles(ArrayList<ICard> deck) throws PileGenerationException;

    /**
     * Creates a market.
     * @param piles The piles of cards.
     * @return The market.
     * @throws MarketGenerationException Thrown if the piles are incorrect for market generation.
     */
    protected abstract IMarket createMarket(ArrayList<IPile> piles) throws MarketGenerationException;

    /**
     * Creates a pile.
     * @param cards The cards in the pile.
     * @return The pile.
     */
    protected abstract IPile createPile(ArrayList<ICard> cards);

    /**
     * Creates a card.
     * @param cardJson The JSON object representing the card.
     * @return The card.
     */
    protected abstract ICard createCard(JSONObject cardJson);

    /**
     * Creates a criteria strategy.
     * @param criteriaJson The JSON object representing the criteria strategy.
     * @return The criteria strategy.
     */
    protected abstract ICriteriaStrategy createCriteria(JSONObject criteriaJson);

    /**
     * Creates a resource.
     * @param resourceJson The JSON object representing the resource.
     * @return The resource.
     */
    protected abstract IResource createResource(JSONObject resourceJson);
}
