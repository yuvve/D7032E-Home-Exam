package game;

import assets.IGameBoard;
import networking.IServer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for creating game loops (templates for running the entire game).
 */
public interface IGameLoopFactory {

        /**
        * Creates a game loop (a template for running the entire game)
         * @param server The server to use
         * @param playerManager The player manager to use
         * @param gameBoard The game board to use
         * @param playerClientMap The player to client map to use
         * @param humanTurns The human turn actions to use
         * @param botTurns The bot turn actions to use
        * @return The game loop
        */
        GameLoopTemplate createGameLoop(
            IServer server,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            Map<Integer, Integer> playerClientMap,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns
        );
}
