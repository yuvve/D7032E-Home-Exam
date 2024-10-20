package game;

import assets.IGameBoard;
import networking.IServer;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Template for a game loop.
 */
public abstract class GameLoopTemplate {
    protected IServer server;
    protected IPlayerManager playerManager;
    protected IGameBoard gameBoard;
    protected Map<Integer, Integer> playerClientMap;
    protected ArrayList<ITurnActionStrategy> humanTurns;
    protected ArrayList<ITurnActionStrategy> botTurns;

    /**
     * Constructor for a game loop template.
     * @param server The server to use.
     * @param playerManager The player manager to use.
     * @param gameBoard The game board to use.
     * @param playerClientMap The player to client map to use.
     */
    public GameLoopTemplate(
            IServer server,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            Map<Integer, Integer> playerClientMap,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns) {
        this.server = server;
        this.playerManager = playerManager;
        this.gameBoard = gameBoard;
        this.playerClientMap = playerClientMap;
        this.humanTurns = humanTurns;
        this.botTurns = botTurns;
    }

    /**
     * Starts the game loop, which runs until the match concludes.
     */
    public void startGame(){
        setupGame();
        while(!gameBoard.hasGameEnded()){
            preRound();
            executeTurn();
            postRound();
        }
        declareWinner();
    }

    /**
     * Executes all turn actions
     */
    private void executeTurn() {
        IPlayer currentPlayer = playerManager.getCurrentPlayer();
        if (currentPlayer.isBot()) {
            for (ITurnActionStrategy botTurn : botTurns) {
                preTurn();
                botTurn.executeTurnAction(currentPlayer);
                postTurn();
            }
        } else {
            for (ITurnActionStrategy humanTurn : humanTurns) {
                preTurn();
                humanTurn.executeTurnAction(currentPlayer);
                postTurn();
            }
        }
    }

    /**
     * Sets up the game.
     */
    protected abstract void setupGame();

    /**
     * Actions to do at the beginning of a round, before any player has taken their turn.
     */
    protected abstract void preRound();

    /**
     * Actions to do at the end of a round, after all players have taken their turns.
     */
    protected abstract void postRound();

    /**
     * Actions to do at the beginning of a player's turn.
     */
    protected abstract void preTurn();

    /**
    * Actions to do at the end of a player's turn.
     */
    protected abstract void postTurn();


    /**
     * Declares the winner of the game.
     */
    protected abstract void declareWinner();
}
