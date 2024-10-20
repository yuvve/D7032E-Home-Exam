package game.impl.turns;

import assets.IGameBoard;
import game.ITurnActionStrategy;
import player.IPlayer;

public class PointSaladBotMain implements ITurnActionStrategy {
    private IGameBoard gameBoard;

    public PointSaladBotMain(IGameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void executeTurnAction(IPlayer player) {

    }
}
