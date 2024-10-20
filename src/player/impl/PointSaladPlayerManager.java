package player.impl;

import assets.ICard;
import assets.ICriteriaStrategy;
import assets.IResource;
import player.IPlayer;
import player.IPlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PointSaladPlayerManager implements IPlayerManager {
    private final ArrayList<IPlayer> players;
    private int currentPlayerIndex;

    /**
     * Constructor for a player manager in the Point Salad game.
     * @param players the players in the game
     */
    public PointSaladPlayerManager(ArrayList<IPlayer> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    @Override
    public void randomizePlayerOrder() {
        Collections.shuffle(players);
        this.currentPlayerIndex = 0;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    @Override
    public int countPlayers() {
        return players.size();
    }

    @Override
    public int countHumanPlayers() {
        int count = 0;
        for (IPlayer player : players) {
            if (!player.isBot()) count++;
        }
        return count;
    }

    @Override
    public int countBotPlayers() {
        int count = 0;
        for (IPlayer player : players) {
            if (player.isBot()) count++;
        }
        return count;
    }

    @Override
    public Map<IPlayer, Integer> calculateScores() {
        Map<IPlayer, Integer> scores = new HashMap<>();
        Map<IPlayer, ArrayList<IResource>> playerResources = new HashMap<>();
        for (IPlayer player : players) {
            playerResources.put(player, getPlayerResources(player));
        }
        for (IPlayer player : players) {
            for (ICriteriaStrategy criteria : getPlayerCriterias(player)) {
                ArrayList<ArrayList<IResource>> otherHands = getAllHandsExceptOne(playerResources, player);
                int score = criteria.calcScore(playerResources.get(player), otherHands);
                scores.put(player, scores.getOrDefault(player, 0) + score);
            }
        }
        return scores;
    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        for (IPlayer player : players) {
            sb.append(player.represent()).append("\n");
        }
        return sb.toString();
    }

    private ArrayList<IResource> getPlayerResources(IPlayer player) {
        ArrayList<IResource> resources = new ArrayList<>();
        for (ICard card : player.getHand()) {
            if (!card.isCriteriaSideActive()) resources.add(card.getResource());
        }
        return resources;
    }

    private ArrayList<ICriteriaStrategy> getPlayerCriterias(IPlayer player) {
        ArrayList<ICriteriaStrategy> criterias = new ArrayList<>();
        for (ICard card : player.getHand()) {
            if (card.isCriteriaSideActive()) criterias.add(card.getCriteriaStrategy());
        }
        return criterias;
    }

    private ArrayList<ArrayList<IResource>> getAllHandsExceptOne(
            Map<IPlayer, ArrayList<IResource>> playerResources, IPlayer excludedPlayer) {
        ArrayList<ArrayList<IResource>> result = new ArrayList<>();

        for (Map.Entry<IPlayer, ArrayList<IResource>> entry : playerResources.entrySet()) {
            if (!entry.getKey().equals(excludedPlayer)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

}
