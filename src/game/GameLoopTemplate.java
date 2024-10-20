package game;

import assets.IGameBoard;
import networking.IServer;
import player.IPlayerManager;

import java.util.Map;

/**
 * Template for a game loop.
 */
public abstract class GameLoopTemplate {
    protected IServer server;
    protected IPlayerManager playerManager;
    protected IGameBoard gameBoard;
    protected Map<Integer, Integer> playerClientMap;

    /**
     * Constructor for a game loop template.
     * @param server The server to use.
     * @param playerManager The player manager to use.
     * @param gameBoard The game board to use.
     * @param playerClientMap The player client map to use.
     */
    public GameLoopTemplate(
            IServer server,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            Map<Integer, Integer> playerClientMap) {
        this.server = server;
        this.playerManager = playerManager;
        this.gameBoard = gameBoard;
        this.playerClientMap = playerClientMap;
    }

    /**
     * Starts the game loop, which runs until the match concludes.
     */
    public void startGame(){
        setupGame();
        while(!gameBoard.hasGameEnded()){
            executeTurn();
        }
        declareWinner();
    }

    /**
     * Sets up the game.
     */
    protected abstract void setupGame();

    /**
     * Executes a turn.
     */
    protected abstract void executeTurn();

    /**
     * Declares the winner of the game.
     */
    protected abstract void declareWinner();
}
