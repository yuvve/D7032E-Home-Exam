package player;

import java.util.ArrayList;

/**
 * Interface for managing player order and turns.
 */
public interface IPlayerManager {

    /**
     * Get the players in the game.
     * @return the players in the game
     */
    ArrayList<IPlayer> getPlayers();

    /**
     * Get the current player.
     * @return the current player
     */
    IPlayer getCurrentPlayer();

    /**
     * Move the turn to the next player.
     */
    void nextTurn();
}
