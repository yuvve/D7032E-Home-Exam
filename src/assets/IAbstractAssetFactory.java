package assets;

import exceptions.IncorrectDeckForPileGeneration;
import exceptions.IncorrectPilesForMarketGeneration;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Interface for an abstract factory that generates assets according to game rules.
 */
public interface IAbstractAssetFactory {

    /**
     * Creates a game board.
     * @param gameBoardJson The JSON object representing the game board.
     * @return The game board.
     * @throws IncorrectDeckForPileGeneration Thrown if any deck is incorrect for pile generation.
     * @throws IncorrectPilesForMarketGeneration Thrown if the piles are incorrect for market generation.
     */
    IGameBoard createGameBoard(JSONObject gameBoardJson)
            throws IncorrectDeckForPileGeneration, IncorrectPilesForMarketGeneration;

    /**
     * Creates piles.
     * @param deck The deck of cards.
     * @return The piles.
     * @throws IncorrectDeckForPileGeneration Thrown if any deck is incorrect for pile generation.
     */
    ArrayList<IPile> createPiles(ArrayList<ICard> deck) throws IncorrectDeckForPileGeneration;

    /**
     * Creates a market.
     * @param piles The piles of cards.
     * @return The market.
     * @throws IncorrectPilesForMarketGeneration Thrown if the piles are incorrect for market generation.
     */
    IMarket createMarket(ArrayList<IPile> piles) throws IncorrectPilesForMarketGeneration;

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
     * @param resourceJson The JSON object representing the resource.
     * @return The resource.
     */
    IResource createResource(JSONObject resourceJson);
}
