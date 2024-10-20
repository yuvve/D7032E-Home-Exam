package game.impl;

import assets.IGameBoard;
import common.point_salad.Constants;
import game.GameLoopTemplate;
import networking.IServer;
import player.IPlayerManager;

import java.util.Map;

public class PointSaladGameLoop extends GameLoopTemplate {
    private final int MIN_PLAYERS = Constants.MIN_PLAYERS.getValue();
    private final int MAX_PLAYERS = Constants.MAX_PLAYERS.getValue();

    public PointSaladGameLoop(
            IServer server,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            Map<Integer, Integer> playerClientMap) {
        super(server, playerManager, gameBoard, playerClientMap);
    }

    protected void setupGame(){
        System.out.println("Setting up Point Salad Game");
        System.out.println("Waiting for players to connect...");
        for (int i = 0; i < playerManager.countPlayers(); i++){
            int clientId = server.acceptClient();
            playerClientMap.put(i, clientId);
        }
    }

    protected void executeTurn(){

    }
    protected void declareWinner(){

    }
}
