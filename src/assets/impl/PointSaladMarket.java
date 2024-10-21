package assets.impl;

import assets.ICard;
import assets.IMarket;
import common.point_salad.Constants;
import exceptions.MarketCardPlacementException;

import java.util.ArrayList;

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
    public ICard viewCard(int row, int column) throws IllegalArgumentException {
        return getCard(row, column, true);
    }

    @Override
    public ICard draftCard(int row, int column) throws IllegalArgumentException {
        return getCard(row, column, false);
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
    public ArrayList<Integer[]> getNonEmptySlotsCoords() {
        ArrayList<Integer[]> nonEmptySlots = new ArrayList<>();
        for (int i = 0; i< cards.length; i++) {
            for (int j = 0; j < cards[0].length; j++) {
                if (cards[i][j] != null){
                    nonEmptySlots.add(new Integer[]{i, j});
                }
            }
        }
        return nonEmptySlots;
    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();

        sb.append(" |");
        for (int i = 0; i < Constants.MARKET_COLS.getValue(); i++) {
            sb.append(" ").append(i).append(" |");
        }
        sb.append("\n");
        for (int i = 0; i < Constants.MARKET_ROWS.getValue(); i++) {
            sb.append(i).append("|");
            for (int j = 0; j < Constants.MARKET_COLS.getValue(); j++) {
                if (cards[i][j] == null){
                    sb.append("   |");
                } else {
                    sb.append(cards[i][j].represent()).append("|");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private ICard getCard(int row, int column, boolean viewOnly) throws IllegalArgumentException {
        if (row < 0 || row >= cards.length || column < 0 || column >= cards[0].length){
            throw new IllegalArgumentException("Invalid coordinates.");
        }
        if (cards[row][column] == null){
            return null;
        }

        ICard card = cards[row][column];
        if (!viewOnly) cards[row][column] = null;
        return card;
    }
}
