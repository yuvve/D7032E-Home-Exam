package game.impl.turns;

import assets.IGameBoard;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;

public class PointSaladHumanFree implements ITurnActionStrategy {
    private IGameBoard gameBoard;
    private IServer server;

    public PointSaladHumanFree(IGameBoard gameBoard, IServer server) {
        this.gameBoard = gameBoard;
        this.server = server;
    }

    @Override
    public void executeTurnAction(IPlayer player) {

    }
}
