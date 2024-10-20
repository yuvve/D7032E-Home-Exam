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
        sb.append("Player ").append(playerId).append(":\n|");
        for (ICard card : hand) {
            sb.append(card.represent()).append("|");
        }
        sb.append("\n");
        return sb.toString();
    }
}
