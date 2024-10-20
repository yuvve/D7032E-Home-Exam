package game.impl;

import assets.IGameBoard;
import game.ITurnActionStrategy;
import game.ITurnActionStrategyFactory;
import game.impl.turns.PointSaladBotFree;
import game.impl.turns.PointSaladBotMain;
import game.impl.turns.PointSaladHumanFree;
import game.impl.turns.PointSaladHumanMain;
import networking.IServer;

import java.util.ArrayList;

public class PointSaladTurnActionStrategyFactory implements ITurnActionStrategyFactory {

    @Override
    public ArrayList<ITurnActionStrategy> createHumanStrategies(IGameBoard gameBoard, IServer server) {
        ArrayList<ITurnActionStrategy> strategies = new ArrayList<>();
        strategies.add(new PointSaladHumanMain(gameBoard, server));
        strategies.add(new PointSaladHumanFree(gameBoard, server));
        return strategies;
    }

    @Override
    public ArrayList<ITurnActionStrategy> createBotStrategies(IGameBoard gameBoard) {
        ArrayList<ITurnActionStrategy> strategies = new ArrayList<>();
        strategies.add(new PointSaladBotMain(gameBoard));
        strategies.add(new PointSaladBotFree(gameBoard));
        return strategies;
    }
}
