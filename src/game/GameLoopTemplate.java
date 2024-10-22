package game;

import assets.IGameBoard;
import io.IIOManager;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Template for a game loop.
 * This class is responsible for running the game loop, which runs until the match concludes.
 * Each step of the game loop is defined in a separate method, which can be overridden by subclasses.
 */
public abstract class GameLoopTemplate {
    protected IIOManager io;
    protected IPlayerManager playerManager;
    protected IGameBoard gameBoard;
    protected ArrayList<ITurnActionStrategy> humanTurns;
    protected ArrayList<ITurnActionStrategy> botTurns;

    /**
     * Constructor for a game loop template.
     * @param io The input output to use.
     * @param playerManager The player manager to use.
     * @param gameBoard The game board to use.
     * @param humanTurns The human turn actions to use.
     * @param botTurns The bot turn actions to use.
     */
    public GameLoopTemplate(
            IIOManager io,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns) {
        this.io = io;
        this.playerManager = playerManager;
        this.gameBoard = gameBoard;
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
            while (!playerManager.roundComplete() && !gameBoard.hasGameEnded()) {
                executeTurn(playerManager.getCurrentPlayer());
                playerManager.nextTurn();
            }
            postRound();
            playerManager.nextRound();
        }
        declareWinner();
    }

    /**
     * Executes all turn actions
     */
    private void executeTurn(IPlayer player) {
        if (player.isBot()) {
            preTurn();
            for (ITurnActionStrategy botTurn : botTurns) {
                botTurn.executeTurnAction(player);
            }
            postTurn();
        } else {
            preTurn();
            for (ITurnActionStrategy humanTurn : humanTurns) {
                humanTurn.executeTurnAction(player);
            }
            postTurn();
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
