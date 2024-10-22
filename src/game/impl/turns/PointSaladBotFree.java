package game.impl.turns;

import assets.ICard;
import assets.IGameBoard;
import game.ITurnActionStrategy;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * The bot action strategy representing a Point Salad free action.
 * The bot will flip a random criteria card (if possible) with a PROB_FLIP_CARD probability.
 */
public class PointSaladBotFree implements ITurnActionStrategy {
    private static final float PROB_FLIP_CARD = 0.0625f;

    private IGameBoard gameBoard;
    private IPlayerManager playerManager;
    private Random random;

    public PointSaladBotFree(IGameBoard gameBoard , IPlayerManager playerManager, Random random) {
        this.gameBoard = gameBoard;
        this.playerManager = playerManager;
        this.random = random;
    }

    @Override
    public void executeTurnAction(IPlayer player) {
        if (random.nextFloat() < PROB_FLIP_CARD) {
            flipCard(player);
        }
    }

    private void flipCard(IPlayer player) {
        ArrayList<ICard> criteriaCards = player.getCriteriaCards();
        if (criteriaCards.isEmpty()) {
            return;
        }
        criteriaCards.get(random.nextInt(criteriaCards.size())).flip();
    }
}
