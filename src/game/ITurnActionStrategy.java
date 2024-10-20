package game;

import assets.IGameBoard;
import networking.IServer;
import player.IPlayerManager;

/**
 * Interface for performing a turn action.
 */
public interface ITurnActionStrategy {

    /**
     * Performs a turn action.
     * @param playerManager The player manager to perform the turn for.
     * @param gameBoard The game board to perform the turn on.
     * @param server The server to perform the turn on.
     * @param clientId The ID of the client performing the turn.
     */
    void performTurn(IPlayerManager playerManager, IGameBoard gameBoard, IServer server, Integer clientId);
}
