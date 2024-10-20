package game.impl;

import assets.IGameBoard;
import common.point_salad.Constants;
import game.GameLoopTemplate;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class PointSaladGameLoop extends GameLoopTemplate {
    private final int MIN_PLAYERS = Constants.MIN_PLAYERS.getValue();
    private final int MAX_PLAYERS = Constants.MAX_PLAYERS.getValue();

    public PointSaladGameLoop(
            IServer server,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            Map<Integer, Integer> playerClientMap,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns) {
        super(server, playerManager, gameBoard, playerClientMap, humanTurns, botTurns);
    }

    protected void setupGame(){
        System.out.println("Setting up Point Salad Game");
        if (playerManager.countHumanPlayers() > 1){
            System.out.println("Waiting for players to connect...");
        }
        for (int i = 0; i < playerManager.countHumanPlayers(); i++){
            int clientId = server.acceptClient();
            playerClientMap.put(i, clientId);
        }
    }

    @Override
    protected void preTurn() {
        server.broadcast(gameBoard.represent());
    }

    @Override
    protected void postTurn() {

    }

    @Override
    protected void declareWinner(){

    }
}
