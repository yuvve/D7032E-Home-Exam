package assets.impl;

import assets.IGameBoard;
import assets.IMarket;
import assets.IPile;

import java.util.ArrayList;

public class PointSaladGameBoard implements IGameBoard {
    private final ArrayList<IPile> piles;
    private final IMarket market;

    /**
     * Constructor for PointSaladGameBoard
     * @param piles the piles of cards
     * @param market the market
     */
    public PointSaladGameBoard(ArrayList<IPile> piles, IMarket market) {
        this.piles = piles;
        this.market = market;
    }

    @Override
    public IPile getPile(int index) throws IllegalArgumentException {
        if (index < 0 || index >= piles.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return piles.get(index);
    }

    @Override
    public IMarket getMarket() {
        return market;
    }

    @Override
    public String represent() {
        // Pretty print the game board
        StringBuilder sb = new StringBuilder();
        sb.append("Piles:\n");
        for (IPile pile : piles) {
            sb.append(pile.represent()).append("\n");
        }
        sb.append("Market:\n").append(market.represent());
        return sb.toString();
    }

    @Override
    public boolean hasGameEnded() {
        if (market.getMarketSize() != 0) {
            return false;
        }
        for (IPile pile : piles) {
            if (pile.getCardCount() != 0) {
                return false;
            }
        }
            return true;
    }
}
