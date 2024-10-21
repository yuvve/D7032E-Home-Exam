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
    protected ArrayList<ITurnActionStrategy> humanTurns;
    protected ArrayList<ITurnActionStrategy> botTurns;
    private Map<Integer, Integer> playerClientMap;

    /**
     * Constructor for a game loop template.
     * @param server The server to use.
     * @param playerManager The player manager to use.
     * @param gameBoard The game board to use.
     * @param playerClientMap The player to client map to use.
     * @param humanTurns The human turn actions to use.
     * @param botTurns The bot turn actions to use.
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

    /** get the client id of a player
     * @param playerId the id of the player
     * @return the client id of the player
     * @throws IllegalArgumentException if the player id is not found in the playerClientMap
     */
    protected int getClientId(int playerId) throws IllegalArgumentException {
        int clientId = playerClientMap.getOrDefault(playerId, -1);
        if (clientId == -1){
            throw new IllegalArgumentException("Player ID not found in playerClientMap");
        }
        return clientId;
    }

    /** map a player to a client
     * @param playerId the id of the player
     * @param clientId the id of the client
     * @throws IllegalArgumentException if the player id already exists in the playerClientMap
     */
    protected void mapPlayerToClient(int playerId, int clientId) throws IllegalArgumentException {
        if (playerClientMap.containsKey(playerId)){
            throw new IllegalArgumentException("Player ID already exists in playerClientMap");
        }
        playerClientMap.put(playerId, clientId);
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
