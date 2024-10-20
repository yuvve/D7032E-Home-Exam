package game.impl;

import assets.IGameBoard;
import common.point_salad.Constants;
import game.GameLoopTemplate;
import networking.IServer;
import player.IPlayerManager;

import java.util.Map;
import java.util.Scanner;

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
        if (playerManager.countHumanPlayers() > 1){
            System.out.println("Waiting for players to connect...");
        }
        for (int i = 0; i < playerManager.countHumanPlayers(); i++){
            int clientId = server.acceptClient();
            playerClientMap.put(i, clientId);
        }
    }

    protected void executeTurn(){
        server.broadcast(gameBoard.represent());
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
    }
    protected void declareWinner(){

    }
}
