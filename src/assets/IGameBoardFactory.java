package assets;

import exceptions.PileGenerationException;
import exceptions.MarketGenerationException;
import org.json.JSONException;

/**
 * Interface for creating game boards factory.
 */
public interface IGameBoardFactory {

    /**
     * Creates a game board.
     * @param deckRawString The JSON string representing the deck of cards.
     * @param numPlayers The number of players.
     * @return The game board.
     * @throws PileGenerationException Thrown if the deck is incorrect for pile generation.
     * @throws MarketGenerationException Thrown if the piles are incorrect for market generation.
     * @throws JSONException Thrown if the string cannot be parsed as JSON.
     */
    IGameBoard createGameBoard(String deckRawString, int numPlayers)
            throws PileGenerationException, MarketGenerationException, JSONException;
}
