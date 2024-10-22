package game.impl;

import assets.IGameBoard;
import game.GameLoopTemplate;
import game.IGameLoopFactory;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;

public class PointSaladGameLoopFactory implements IGameLoopFactory {
    @Override
    public GameLoopTemplate createGameLoop(
            IServer server,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            Map<Integer, Integer> playerClientMap,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns) {
        return new PointSaladGameLoop(server, playerManager, gameBoard, playerClientMap, humanTurns, botTurns);
    }
}
