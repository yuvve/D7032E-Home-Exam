package game.impl.turns;

import assets.IGameBoard;
import game.ITurnActionStrategy;
import player.IPlayer;

public class PointSaladBotFree implements ITurnActionStrategy {
    private IGameBoard gameBoard;

    public PointSaladBotFree(IGameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void executeTurnAction(IPlayer player) {

    }
}
