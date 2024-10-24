package game;

import player.IPlayer;

/**
 * Interface for performing a turn action.
 */
public interface ITurnActionStrategy {

    /**
     * Performs a turn action.
     * @param player The player to perform the turn action.
     */
    void executeTurnAction(IPlayer player);
}
