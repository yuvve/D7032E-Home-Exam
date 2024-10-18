package player.impl;

import player.IPlayer;
import player.IPlayerManager;
import player.IPlayerManagerFactory;

import java.util.ArrayList;

public class PointSaladPlayerManagerFactory implements IPlayerManagerFactory {
    private final PointSaladPlayerFactory playerFactory = new PointSaladPlayerFactory();

    @Override
    public IPlayerManager createPlayerManager(int numHumans, int numBots) {
        int numTotalPlayers = numHumans + numBots;
        if (numTotalPlayers < 2 || numTotalPlayers > 6) {
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
