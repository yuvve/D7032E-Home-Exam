package player;

import java.util.Map;

/**
 * Interface for managing player order and turns.
 */
public interface IPlayerManager {

    /**
     * Randomize the order of the players.
     */
    void randomizePlayerOrder();

    /**
     * Get the current player.
     * @return the current player
     */
    IPlayer getCurrentPlayer();

    /**
     * Move the turn to the next player.
     */
    void nextTurn();

    /**
     * Get the number of players in the game.
     * @return the number of players in the game
     */
    int countPlayers();

    /**
     * Calculate the scores of all players.
     * @return a map of players to their scores
     */
    Map<IPlayer, Integer> calculateScores();
}
