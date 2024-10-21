package game.impl.turns;

import assets.IGameBoard;
import game.ITurnActionStrategy;
import player.IPlayer;
import player.IPlayerManager;

public class PointSaladBotFree implements ITurnActionStrategy {
    private IGameBoard gameBoard;
    private IPlayerManager playerManager;

    public PointSaladBotFree(IGameBoard gameBoard , IPlayerManager playerManager) {
        this.gameBoard = gameBoard;
        this.playerManager = playerManager;
    }

    @Override
    public void executeTurnAction(IPlayer player) {

    }
}
