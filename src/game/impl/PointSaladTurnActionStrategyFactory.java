package game.impl;

import assets.IGameBoard;
import io.IIOManager;
import game.ITurnActionStrategy;
import game.ITurnActionStrategyFactory;
import game.impl.turns.PointSaladBotFree;
import game.impl.turns.PointSaladBotMain;
import game.impl.turns.PointSaladHumanFree;
import game.impl.turns.PointSaladHumanMain;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Random;

public class PointSaladTurnActionStrategyFactory implements ITurnActionStrategyFactory {

    @Override
    public ArrayList<ITurnActionStrategy> createHumanStrategies(
            IGameBoard gameBoard,
            IIOManager io) {
        ArrayList<ITurnActionStrategy> strategies = new ArrayList<>();
        strategies.add(new PointSaladHumanMain(gameBoard, io));
        strategies.add(new PointSaladHumanFree(gameBoard, io));
        return strategies;
    }

    @Override
    public ArrayList<ITurnActionStrategy> createBotStrategies(
            IGameBoard gameBoard, IPlayerManager playerManager, Random random) {
        ArrayList<ITurnActionStrategy> strategies = new ArrayList<>();
        strategies.add(new PointSaladBotMain(gameBoard, playerManager, random));
        strategies.add(new PointSaladBotFree(gameBoard, playerManager, random));
        return strategies;
    }
}
