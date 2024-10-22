package game;

import assets.IGameBoard;
import io.IIOManager;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Factory for creating turn action strategies.
 */
public interface ITurnActionStrategyFactory {

    /**
     * Creates the actions for a human turn.
     * @param gameBoard The game board to perform the turn action on.
     * @param io The IOManager to use.
     * @return The human turn actions
     */
    ArrayList<ITurnActionStrategy> createHumanStrategies(
            IGameBoard gameBoard,
            IIOManager io);

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
