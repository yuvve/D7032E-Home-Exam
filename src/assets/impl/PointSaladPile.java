package assets.impl;

import assets.ICard;
import assets.IPile;

import java.util.ArrayList;
import java.util.Collections;

public class PointSaladPile implements IPile {
    private final ArrayList<ICard> cards;

    public PointSaladPile(ArrayList<ICard> cards) {
        this.cards = cards;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public int getCardCount() {
        return cards.size();
    }

    @Override
    public ICard viewTop() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.getFirst();
    }

    @Override
    public ICard drawTop() {
        if (cards.isEmpty()) {
            return null;
        }
        ICard card = cards.removeFirst();
        cards.getFirst().flip();
        return card;
    }

    @Override
    public ICard drawBottom() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.removeLast();
    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(cards.size()).append(" cards)").append("\n");
        sb.append(viewTop().represent()).append("\n");
        return sb.toString();
    }
}
