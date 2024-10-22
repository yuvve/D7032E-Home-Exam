package assets.impl;

import assets.ICard;
import assets.IPile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PointSaladPile implements IPile {
    private final ArrayList<ICard> cards;
    private Random random;

    public PointSaladPile(ArrayList<ICard> cards, Random random) {
        this.cards = cards;
        this.random = random;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    @Override
    public int size() {
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
    public void addCards(ArrayList<ICard> cards) {
        this.cards.addAll(cards);
    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(cards.size()).append(" cards)").append("\n");
        if (!cards.isEmpty()) {
            sb.append(viewTop().represent()).append("\n");
        }
        return sb.toString();
    }
}
