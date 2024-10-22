package assets.impl;

import assets.ICard;
import assets.IGameBoard;
import assets.IMarket;
import assets.IPile;
import common.point_salad.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public ICard getCardFromPile(int pileIndex) throws IllegalArgumentException {
        IPile pile = getPile(pileIndex);
        ICard card = pile.drawTop();
        if (pile.getCardCount() == 0) {
            rebalancePiles();
        }
        return card;
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
    public void refillMarket() {
        if (getNonEmptyPiles().isEmpty()) {
            return;
        }
        int[] marketSize = market.getMarketSize();
        for (int row = 0; row < marketSize[0]; row++){
            for (int col = 0; col < marketSize[1]; col++){
                if (market.viewCard(row, col) == null){
                    ICard card = getCardFromPiles(col);
                    market.placeCardInPosition(card, row, col);
                    if (card != null) {
                        card.flip();
                    }
                }
            }
        }
    }

    private ICard getCardFromPiles(int col) {
        IPile pile = piles.get(col);
        ICard card = pile.drawTop();
        if (pile.getCardCount() == 0) {
            rebalancePiles();
        }
        return card;
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

    private void rebalancePiles() {
        while (hasEmptyPile() && !allPilesEmpty() && hasPileWithMoreThanOneCard()) {
            int maxIndex = findLargestPileIndex();
            int emptyIndex = findEmptyPileIndex();
            IPile largestPile = piles.get(maxIndex);
            IPile emptyPile = piles.get(emptyIndex);

            ArrayList<ICard> cardsTakenFromMaxPile = new ArrayList<>();
            int halfSize = largestPile.getCardCount() / 2;
            for (int i = 0; i < halfSize; i++) {
                cardsTakenFromMaxPile.add(largestPile.drawBottom());
            }
            emptyPile.addCards(cardsTakenFromMaxPile);
        }
    }

    private boolean hasPileWithMoreThanOneCard() {
        for (IPile pile : piles) {
            if (pile.getCardCount() > 1) {
                return true;
            }
        }
        return false;
    }

    private boolean allPilesEmpty() {
        for (IPile pile : piles) {
            if (pile.getCardCount() != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean hasEmptyPile() {
        if (findEmptyPileIndex() == -1) {
            return false;
        }
        return true;
    }

    private int findEmptyPileIndex() {
        for (int i = 0; i < piles.size(); i++) {
            if (piles.get(i).getCardCount() == 0) {
                return i;
            }
        }
        return -1;
    }

    private int findLargestPileIndex() {
        int maxIndex = 0;
        int maxSize = 0;
        for (int i = 0; i < piles.size(); i++) {
            if (piles.get(i).getCardCount() > maxSize) {
                maxSize = piles.get(i).getCardCount();
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private IPile getPile(int index) throws IllegalArgumentException {
        if (index < 0 || index >= piles.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return piles.get(index);
    }
}
