package player.impl;

import assets.ICard;
import exceptions.CardDiscardingException;
import exceptions.CardFlippingException;
import player.IPlayer;

import java.util.ArrayList;

public class PointSaladPlayer implements IPlayer {
    private final int playerId;
    private boolean isBot;
    private ArrayList<ICard> hand;

    /**
     * Constructor for a player in the Point Salad game.
     * @param playerId the player's ID
     * @param isBot true if the player is a bot, false otherwise
     * @param hand the player's hand (can be empty or null)
     */
    public PointSaladPlayer(int playerId, boolean isBot, ArrayList<ICard> hand) {
        if (hand == null) {
            hand = new ArrayList<>();
        }
        this.playerId = playerId;
        this.isBot = isBot;
        this.hand = hand;
    }

    @Override
    public int getId() {
        return playerId;
    }

    @Override
    public boolean isBot() {
        return isBot;
    }

    @Override
    public void makeBot() {
        isBot = true;
    }

    @Override
    public ArrayList<ICard> getHand() {
        return hand;
    }

    @Override
    public ArrayList<ICard> getResourceCards() {
        ArrayList<ICard> vegetableCards = new ArrayList<>();

        for (ICard card: hand) {
            if (!card.isCriteriaSideActive()) {
                vegetableCards.add(card);
            }
        }
        return vegetableCards;
    }

    @Override
    public ArrayList<ICard> getCriteriaCards() {
        ArrayList<ICard> criteriaCards = new ArrayList<>();

        for (ICard card: hand) {
            if (card.isCriteriaSideActive()) {
                criteriaCards.add(card);
            }
        }
        return criteriaCards;
    }

    @Override
    public void discard(ICard card) {
        if (!hand.contains(card)) {
            throw new CardDiscardingException("Player does not have the card to discard.");
        }
        hand.remove(card);
    }

    @Override
    public void addToHand(ICard card) {
        hand.add(card);
    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        if (hand.isEmpty()) {
            sb.append("Hand is empty.");
            return sb.toString();
        }
        ArrayList<ICard> criteriaCards = new ArrayList<>();
        ArrayList<ICard> vegetableCards = new ArrayList<>();

        for (ICard card: hand) {
            if (card.isCriteriaSideActive()) {
                criteriaCards.add(card);
            }
            else {
                vegetableCards.add(card);
            }
        }
        sb.append("Criteria Cards:").append("\n");
        sb.append("-----------------------------\n");
        if (!criteriaCards.isEmpty()) {
            for (ICard card : criteriaCards) {
                sb.append("Card ").append(hand.indexOf(card)).append(":\n");
                sb.append(card.represent());
                sb.append("\n");
            }
        }
        sb.append("-----------------------------").append("\n").append("\n");

        sb.append("Vegetable Cards (with card NUMBER below):").append("\n");
        sb.append("-----------------------------").append("\n");
        if (!vegetableCards.isEmpty()) {
            sb.append("|");
            for (ICard card : vegetableCards) {
                sb.append(card.represent()).append("|");
            }

            sb.append("\n").append("|");
            for (ICard card : vegetableCards) {
                sb.append(" ").append(hand.indexOf(card)).append(" |");
            }
        }
        sb.append("\n").append("-----------------------------");
        return sb.toString();
    }
}
