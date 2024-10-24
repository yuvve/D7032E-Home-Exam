package game;

import assets.IGameBoard;
import io.IIOManager;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for creating game loops (templates for running the entire game).
 */
public interface IGameLoopFactory {

        /**
        * Creates a game loop (a template for running the entire game)
         * @param io The IO-manager to use
         * @param playerManager The player manager to use
         * @param gameBoard The game board to use
         * @param humanTurns The human turn actions to use
         * @param botTurns The bot turn actions to use
        * @return The game loop
        */
        GameLoopTemplate createGameLoop(
            IIOManager io,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns
        );
}
