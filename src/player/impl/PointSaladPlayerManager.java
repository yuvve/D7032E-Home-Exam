package player.impl;

import common.point_salad.Constants;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Collections;

public class PointSaladPlayerManager implements IPlayerManager {
    private final int MIN_PLAYERS = Constants.MIN_PLAYERS.getValue();
    private final int MAX_PLAYERS = Constants.MAX_PLAYERS.getValue();
    
    private final ArrayList<IPlayer> players;
    private IPlayer currentPlayer;
    private int currentPlayerIndex;

    /**
     * Constructor for a player manager in the Point Salad game.
     * @param players the players in the game
     *                Must be between 2 and 6 players
     */
    public PointSaladPlayerManager(ArrayList<IPlayer> players) {
        if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) {
            throw new IllegalArgumentException("Invalid number of players.");
        }
        this.players = players;
        Collections.shuffle(this.players);
        this.currentPlayer = players.getFirst();
        this.currentPlayerIndex = 0;
    }

    @Override
    public ArrayList<IPlayer> getPlayers() {
        return players;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }

}
