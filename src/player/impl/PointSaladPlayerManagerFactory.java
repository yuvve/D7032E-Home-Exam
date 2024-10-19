package player.impl;

import common.point_salad.Constants;
import player.IPlayer;
import player.IPlayerManager;
import player.IPlayerManagerFactory;

import java.util.ArrayList;

public class PointSaladPlayerManagerFactory implements IPlayerManagerFactory {
    private static final int MIN_PLAYERS = Constants.MIN_PLAYERS.getValue();
    private static final int MAX_PLAYERS = Constants.MAX_PLAYERS.getValue();

    @Override
    public IPlayerManager createPlayerManager(int numHumans, int numBots) {
        PointSaladPlayerFactory playerFactory = new PointSaladPlayerFactory();
        int numTotalPlayers = numHumans + numBots;
        if (numTotalPlayers < MIN_PLAYERS || numTotalPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException("Invalid number of players (humans + bots).");
        }
        ArrayList<IPlayer> players = new ArrayList<>();
        for (int i = 0; i < numHumans; i++) {
            players.add(playerFactory.createPlayer(false));
        }
        for (int i = numHumans; i < numTotalPlayers; i++) {
            players.add(playerFactory.createPlayer(true));
        }
        return new PointSaladPlayerManager(players);
    }
}
