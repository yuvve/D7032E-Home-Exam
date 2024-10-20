package assets.impl;

import assets.ICard;
import assets.IMarket;
import common.point_salad.Constants;
import exceptions.MarketCardPlacementException;

public class PointSaladMarket implements IMarket {
    private final ICard[][] cards;

    /**
     * Constructor for PointSaladMarket
     * @param cards the initial cards in the market
     */
    public PointSaladMarket(ICard[][] cards) {
        this.cards = cards;
    }


    @Override
    public ICard draftCard(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row >= cards.length || column < 0 || column >= cards[0].length){
            throw new IllegalArgumentException("Invalid coordinates.");
        }
        if (cards[row][column] == null){
            return null;
        }

        ICard card = cards[row][column];
        cards[row][column] = null;
        return card;
    }

    @Override
    public void placeCardInPosition(ICard card, int row, int column)
            throws MarketCardPlacementException, IllegalArgumentException {
        if (row < 0 || row >= cards.length || column < 0 || column >= cards[0].length){
            throw new IllegalArgumentException("Invalid coordinates.");
        }
        if (cards[row][column] != null){
            throw new MarketCardPlacementException("The position is already occupied.");
        }
        cards[row][column] = card;
    }


    @Override
    public int getMarketSize() {
        int count = 0;
        for (ICard[] card : cards) {
            for (ICard iCard : card) {
                if (iCard != null) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String represent() {
        StringBuilder marketString = new StringBuilder();
        marketString.append(" |");
        for (int i = 0; i < Constants.MARKET_COLS.getValue(); i++) {
            marketString.append(" ").append(i+1).append(" |");
        }
        marketString.append("\n");
        for (int i = 0; i < Constants.MARKET_ROWS.getValue(); i++) {
            marketString.append(i+1).append("|");
            for (int j = 0; j < Constants.MARKET_COLS.getValue(); j++) {
                if (cards[i][j] == null){
                    marketString.append("   |");
                } else {
                    marketString.append(cards[i][j].represent()).append("|");
                }
            }
            marketString.append("\n");
        }
        return marketString.toString();
    }
}
