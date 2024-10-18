package player.impl;

import player.IPlayer;
import player.IPlayerFactory;

public class PointSaladPlayerFactory implements IPlayerFactory {
    private int currentPlayerId = 0;

    @Override
    public IPlayer createPlayer(boolean isBot) {
        return new PointSaladPlayer(currentPlayerId++, isBot);
    }
}

