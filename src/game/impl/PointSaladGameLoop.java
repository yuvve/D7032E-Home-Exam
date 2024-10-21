package game.impl;

import assets.IGameBoard;
import game.GameLoopTemplate;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;

public class PointSaladGameLoop extends GameLoopTemplate {

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
        server.startServer(8080);
        if (playerManager.countHumanPlayers() > 1){
            System.out.println("Waiting for players to connect...");
        }
        for (int i = 0; i < playerManager.countHumanPlayers(); i++){
            int clientId = server.acceptClient();
            playerClientMap.put(i, clientId);
        }
    }

    @Override
    protected void preRound() {
        server.broadcast(playerManager.represent());
        server.broadcast(gameBoard.represent());
    }

    @Override
    protected void postRound() {
        gameBoard.refillMarket();
    }

    @Override
    protected void preTurn() {

    }

    @Override
    protected void postTurn() {
        //TODO: refill the market
    }

    @Override
    protected void declareWinner(){

    }
}
