package game.impl.turns;

import assets.IGameBoard;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;

import java.util.Map;

public class PointSaladHumanFree implements ITurnActionStrategy {
    private IGameBoard gameBoard;
    private IServer server;
    private Map<Integer, Integer> playerClientMap;

    public PointSaladHumanFree(IGameBoard gameBoard, IServer server, Map<Integer, Integer> playerClientMap) {
        this.gameBoard = gameBoard;
        this.server = server;
        this.playerClientMap = playerClientMap;
    }

    @Override
    public void executeTurnAction(IPlayer player) {

    }
}
