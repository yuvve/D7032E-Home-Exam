package player;

import common.IRepresentable;

import java.util.Map;

/**
 * Interface for managing player order and turns.
 */
public interface IPlayerManager extends IRepresentable {

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
     * Get the number of human players in the game.
     * @return the number of human players in the game
     */
    int countHumanPlayers();

    /**
     * Get the number of bot players in the game.
     * @return the number of bot players in the game
     */
    int countBotPlayers();

    /**
     * Calculate the scores of all players.
     * @return a map of players to their scores
     */
    Map<IPlayer, Integer> calculateScores();
}
