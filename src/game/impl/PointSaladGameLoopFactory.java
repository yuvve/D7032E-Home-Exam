package game.impl;

import assets.IGameBoard;
import game.GameLoopTemplate;
import game.IGameLoopFactory;
import game.ITurnActionStrategy;
import io.IIOManager;
import networking.IServer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Map;

public class PointSaladGameLoopFactory implements IGameLoopFactory {
    @Override
    public GameLoopTemplate createGameLoop(
            IIOManager io,
            IPlayerManager playerManager,
            IGameBoard gameBoard,
            ArrayList<ITurnActionStrategy> humanTurns,
            ArrayList<ITurnActionStrategy> botTurns) {
        return new PointSaladGameLoop(io, playerManager, gameBoard, humanTurns, botTurns);
    }
}
