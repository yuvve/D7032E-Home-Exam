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
    public ArrayList<IPile> getPiles() {
        return piles;
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
}
