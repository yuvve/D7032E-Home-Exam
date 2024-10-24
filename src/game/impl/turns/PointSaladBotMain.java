package game.impl.turns;

import assets.IGameBoard;
import assets.IPile;
import game.ITurnActionStrategy;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * The bot action strategy representing a Point Salad main action.
 * The bot will take a card from a random pile with probability PROB_TAKE_FROM_PILE, otherwise take from market.
 * If one option is not possible, then the bot will take from the other one.
 * The bot will take two cards from the market with a PROB_TAKE_TWICE_FROM_MARKET probability.
 */
public class PointSaladBotMain implements ITurnActionStrategy {
    private static final float PROB_TAKE_FROM_PILE = 0.25f;
    private static final float PROB_TAKE_TWICE_FROM_MARKET = 0.5f;

    private IGameBoard gameBoard;
    private IPlayerManager playerManager;
    private Random random;

    public PointSaladBotMain(IGameBoard gameBoard , IPlayerManager playerManager, Random random) {
        this.gameBoard = gameBoard;
        this.playerManager = playerManager;
        this.random = random;
    }

    @Override
    public void executeTurnAction(IPlayer player) {
        ArrayList<Integer> nonEmptyPiles = gameBoard.getNonEmptyPiles();
        ArrayList<Integer[]> nonEmptyMarketCoords = gameBoard.getMarket().getNonEmptySlotsCoords();

        if (!nonEmptyPiles.isEmpty() && !nonEmptyMarketCoords.isEmpty()){
            boolean takeFromPile = random.nextFloat() < PROB_TAKE_FROM_PILE;
            if (takeFromPile) takeFromPiles(nonEmptyPiles);
            else takeFromMarket(nonEmptyMarketCoords);
        } else if (!nonEmptyMarketCoords.isEmpty()) {
            takeFromMarket(nonEmptyMarketCoords);
        } else if (!nonEmptyPiles.isEmpty()) {
            takeFromPiles(nonEmptyPiles);
        }
    }

    private void takeFromPiles(ArrayList<Integer> nonEmptyPiles) {
        int pileIndex = nonEmptyPiles.get(random.nextInt(nonEmptyPiles.size()));
        playerManager.getCurrentPlayer().addToHand(gameBoard.getCardFromPile(pileIndex));
    }

    private void takeFromMarket(ArrayList<Integer[]> nonEmptyMarketCoords) {
        Integer[] coords = nonEmptyMarketCoords.get(
                random.nextInt(nonEmptyMarketCoords.size())
        );
        playerManager.getCurrentPlayer().addToHand(gameBoard.getMarket().draftCard(coords[0], coords[1]));

        if (random.nextFloat() < PROB_TAKE_TWICE_FROM_MARKET) {
            nonEmptyMarketCoords.remove(coords);
            if (nonEmptyMarketCoords.isEmpty()) return;
            coords = nonEmptyMarketCoords.get(
                    random.nextInt(nonEmptyMarketCoords.size())
            );
            playerManager.getCurrentPlayer().addToHand(gameBoard.getMarket().draftCard(coords[0], coords[1]));
        }
    }
}
