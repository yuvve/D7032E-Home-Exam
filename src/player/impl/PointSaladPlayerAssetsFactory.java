package player.impl;

import player.IAbstractPlayerAssetsFactory;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Random;

public class PointSaladPlayerAssetsFactory implements IAbstractPlayerAssetsFactory {
    private int currentPlayerId = 0;
    private Random random;

    public PointSaladPlayerAssetsFactory(Random random) {
        this.random = random;
    }

    @Override
    public IPlayer createPlayer(boolean isBot) {
        return new PointSaladPlayer(currentPlayerId++, isBot, null);
    }

    @Override
    public IPlayerManager createPlayerManager(int numHumanPlayers, int numBotPlayers) {
        ArrayList<IPlayer> players = new ArrayList<>();
        for (int i = 0; i < numHumanPlayers; i++) {
            players.add(createPlayer(false));
        }
        for (int i = 0; i < numBotPlayers; i++) {
            players.add(createPlayer(true));
        }
        PointSaladPlayerManager playerManager = new PointSaladPlayerManager(players, random);
        playerManager.randomizePlayerOrder();
        return playerManager;
    }
}
