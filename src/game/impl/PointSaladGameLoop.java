package game.impl;

import assets.IGameBoard;
import game.GameLoopTemplate;
import game.ITurnActionStrategy;
import io.IIOManager;
import player.IPlayer;
import player.IPlayerManager;

import java.util.*;

public class PointSaladGameLoop extends GameLoopTemplate {
    private int currRound;

    public PointSaladGameLoop(
            IIOManager io,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns) {
        super(io, playerManager, gameBoard, humanTurns, botTurns);
        currRound = 1;
    }

    protected void setupGame(){
        System.out.println("Setting up Point Salad Game");
        if (playerManager.countHumanPlayers() > 1){
            System.out.println("Waiting for players to connect...");
        }

        for (int i = 0; i < playerManager.countHumanPlayers(); i++){
            io.registerPlayer(i);
        }
    }

    @Override
    protected void preRound() {
        io.broadcast("Starting round " + currRound++ + "...");
    }

    @Override
    protected void postRound() {

    }

    @Override
    protected void preTurn() {
        IPlayer currPlayer = playerManager.getCurrentPlayer();
        if (currPlayer.isBot()) return;

        io.sendMsg(currPlayer.getId(), playerManager.represent());
        io.sendMsg(currPlayer.getId(), gameBoard.represent());
    }

    @Override
    protected void postTurn() {
        gameBoard.refillMarket();
        IPlayer currPlayer = playerManager.getCurrentPlayer();
        if (!currPlayer.isBot() && playerManager.countHumanPlayers() > 1) {
            io.sendMsg(
                    currPlayer.getId(),
                    "Waiting for other players to finish their turn...");
        }
    }

    @Override
    protected void declareWinner() {
        io.broadcast(playerManager.represent());
        io.broadcast("Game over! Calculating scores...");
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
        io.broadcast(sb.toString());
        if (winner.isBot()) {
            io.broadcast("The winner is a bot!");
        } else {
            io.sendMsg(winner.getId(), "Congratulations! You are the winner!");
        }
        io.broadcast("Game over! Closing connection...");
        io.endGame();
    }

}
