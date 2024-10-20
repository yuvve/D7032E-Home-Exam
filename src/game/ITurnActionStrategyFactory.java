package game;

import assets.IGameBoard;
import networking.IServer;

import java.util.ArrayList;

/**
 * Factory for creating turn action strategies.
 */
public interface ITurnActionStrategyFactory {

    /**
     * Creates the actions for a human turn.
     * @param gameBoard The game board to perform the turn action on.
     * @param server The server to use.
     * @return The human turn actions
     */
    ArrayList<ITurnActionStrategy> createHumanStrategies(IGameBoard gameBoard, IServer server);

    /**
     * Creates the actions for a bot turn.
     * @param gameBoard The game board to perform the turn action on.
     * @return The bot turn actions
     */
    ArrayList<ITurnActionStrategy> createBotStrategies(IGameBoard gameBoard);
}
