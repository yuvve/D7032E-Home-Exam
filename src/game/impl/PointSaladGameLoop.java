package game.impl;

import assets.IGameBoard;
import game.GameLoopTemplate;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;
import player.IPlayerManager;

import java.util.*;

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
        if (playerManager.countHumanPlayers() > 1){
            System.out.println("Waiting for players to connect...");
        }

        for (int i = 0; i < playerManager.countHumanPlayers(); i++){
            int clientId = server.acceptClient();
            mapPlayerToClient(i, clientId);
        }
    }

    @Override
    protected void preRound() {

    }

    @Override
    protected void postRound() {

    }

    @Override
    protected void preTurn() {
        IPlayer currPlayer = playerManager.getCurrentPlayer();
        if (currPlayer.isBot()) return;

        int currPlayerClientId = getClientId(currPlayer.getId());
        server.sendMsg(currPlayerClientId, playerManager.represent());
        server.sendMsg(currPlayerClientId, gameBoard.represent());
    }

    @Override
    protected void postTurn() {
        gameBoard.refillMarket();
    }

    @Override
    protected void declareWinner() {
        server.broadcast(playerManager.represent());
        server.broadcast("Game over! Calculating scores...");
        Map<IPlayer, Integer> playerToScore = playerManager.calculateScores();
        List<Map.Entry<IPlayer, Integer>> sortedPlayerList = new ArrayList<>(playerToScore.entrySet());

        // Sort the list by values (scores) in descending order
        sortedPlayerList.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        // Store the sorted entries in a LinkedHashMap to maintain the order
        LinkedHashMap<IPlayer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<IPlayer, Integer> entry : sortedPlayerList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        IPlayer winner = sortedPlayerList.getFirst().getKey();

        StringBuilder sb = new StringBuilder();
        sb.append("The winner is Player ").append(winner.getId()).append("!\n");
        for (IPlayer player : sortedMap.keySet()) {
            sb.append("Player ").append(player.getId())
                    .append(" has scored ").append(sortedMap.get(player)).append(" points\n");
        }
        server.broadcast(sb.toString());
        if (winner.isBot()) {
            server.broadcast("The winner is a bot!");
        } else {
            server.sendMsg(getClientId(winner.getId()), "Congratulations! You are the winner!");
        }
    }

}
