package assets.impl;

import assets.ICard;
import assets.ICriteriaStrategy;
import assets.IPile;

import java.util.ArrayList;
import java.util.Collections;

public class PointSaladPile implements IPile {
    private ArrayList<ICard> cards;

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public int getCardCount() {
        return cards.size();
    }

    @Override
    public ICard drawTop() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.removeFirst();
    }

    @Override
    public ICard drawBottom() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.removeLast();
    }

    @Override
    public ICriteriaStrategy viewTop() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.getFirst().getCriteriaStrategy();
    }

    @Override
    public String represent() {
        return "A pile with " + cards.size() + " cards. The top card is: " + viewTop().represent();
    }
}
