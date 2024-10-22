package game;

import assets.IGameBoard;
import networking.IServer;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Factory for creating turn action strategies.
 */
public interface ITurnActionStrategyFactory {

    /**
     * Creates the actions for a human turn.
     * @param gameBoard The game board to perform the turn action on.
     * @param server The server to use.
     * @param playerClientMap a map of player id to client id
     * @return The human turn actions
     */
    ArrayList<ITurnActionStrategy> createHumanStrategies(
            IGameBoard gameBoard,
            IServer server,
            Map<Integer, Integer> playerClientMap);

    /**
     * Creates the actions for a bot turn.
     * @param gameBoard The game board to perform the turn action on.
     * @param playerManager The player manager of the match, can allow the bot to make smarter decisions.
     * @param random The random number generator to use.
     * @return The bot turn actions
     */
    ArrayList<ITurnActionStrategy> createBotStrategies(
            IGameBoard gameBoard,
            IPlayerManager playerManager,
            Random random);
}
