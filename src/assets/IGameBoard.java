package assets;

import common.IRepresentable;
import exceptions.IncorrectDeckForPileGeneration;
import exceptions.IncorrectPilesForMarketGeneration;

import java.util.ArrayList;

/**
 * Interface for what is considered in the game-domain a game board.
 * Contains the card pile(s) and the market.
 */
public interface IGameBoard extends IRepresentable {
    /**
     * Gets the piles of cards.
     * @return The piles of cards.
     */
    ArrayList<IPile> getPiles();

    /**
     * Gets the market.
     * @return The market.
     */
    IMarket getMarket();

}
