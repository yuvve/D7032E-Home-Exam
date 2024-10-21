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
    public ArrayList<Integer> getNonEmptyPiles() {
        ArrayList<Integer> nonEmptyPilesIndexes = new ArrayList<>();
        for (int i = 0; i < piles.size(); i++) {
            if (piles.get(i).getCardCount() != 0) {
                nonEmptyPilesIndexes.add(i);
            }
        }
        return nonEmptyPilesIndexes;
    }

    @Override
    public IMarket getMarket() {
        return market;
    }

    @Override
    public boolean hasGameEnded() {
        if (!market.getNonEmptySlotsCoords().isEmpty()) {
            return false;
        }
        for (IPile pile : piles) {
            if (pile.getCardCount() != 0) {
                return false;
            }
        }
            return true;
    }

    @Override
    public String represent() {
        // Pretty print the game board
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < piles.size(); i++) {
            sb.append("Pile ").append(i).append(":\n");
            sb.append("-----------------------------\n");
            sb.append(piles.get(i).represent());
            sb.append("-----------------------------\n");
            sb.append("\n");
        }
        sb.append("Market:\n");
        sb.append("-----------------------------\n");
        sb.append(market.represent());
        sb.append("-----------------------------\n");
        return sb.toString();
    }
}
