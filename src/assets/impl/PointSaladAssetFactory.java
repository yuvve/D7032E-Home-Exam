package assets.impl;

import assets.*;
import assets.impl.criterias.*;
import exceptions.DeckGenerationException;
import exceptions.PileGenerationException;
import exceptions.MarketGenerationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class PointSaladAssetFactory extends AbstractAssetFactory {
    private static final int NUM_PLAYERS_MIN = 2;
    private static final int NUM_PLAYERS_MAX = 6;
    private static final int CARDS_TO_REMOVE_PER_PLAYER_MISSING = 3;
    private static final int NUM_PILES = 3;
    private static final int DECK_SIZE = 108;
    private static final int MARKET_ROWS = 3;
    private static final int MARKET_COLS = 2;
    private static final int NUM_RESOURCES = 6;

    @Override
    public IGameBoard createGameBoard(String deckRawString, int numPlayers)
            throws PileGenerationException, MarketGenerationException, JSONException {

        JSONObject deckJson = new JSONObject(deckRawString);
        ArrayList<ICard> deckCards = createDeck(deckJson, numPlayers);
        ArrayList<IPile> piles = createPiles(deckCards);
        IMarket market = createMarket(piles);

        return new PointSaladGameBoard(piles, market);
    }

    @Override
    protected ArrayList<ICard> createDeck(JSONObject deckJson, int numPlayers)
            throws DeckGenerationException {

        if (numPlayers < NUM_PLAYERS_MIN || numPlayers > NUM_PLAYERS_MAX) {
            throw new DeckGenerationException("Invalid number of players.");
        }

        ArrayList<ICard> cards = new ArrayList<>();
        for (String cardName : deckJson.keySet()) {
            JSONObject cardJson = deckJson.getJSONObject(cardName);
            ICard card = createCard(cardJson);
            cards.add(card);
        }

        if (cards.size() != DECK_SIZE) {
            throw new DeckGenerationException("Incorrect number of cards in deck.");
        }

        if (numPlayers == 6) {
            Collections.shuffle(cards);
            return cards;
        }

        Map<IResource, ArrayList<ICard>> resourcePiles = sortCardsByResource(cards);
        for (ArrayList<ICard> cardsPile: resourcePiles.values()) {
            Collections.shuffle(cardsPile);
            int numCardsToRemove = CARDS_TO_REMOVE_PER_PLAYER_MISSING * (NUM_PLAYERS_MAX - numPlayers);
            for (int i = 0; i < numCardsToRemove; i++) {
                cardsPile.removeFirst();
            }
        }

        ArrayList<ICard> deck = new ArrayList<>();
        for (ArrayList<ICard> resourceCards : resourcePiles.values()) {
            deck.addAll(resourceCards);
        }

        return deck;
    }

    @Override
    protected ArrayList<IPile> createPiles(ArrayList<ICard> deck) throws PileGenerationException {
        Collections.shuffle(deck);
        ArrayList<IPile> piles = new ArrayList<>();

        int numCardsPerPile = deck.size() / NUM_PILES;
        int numCardsLeft = deck.size() % NUM_PILES;
        int start = 0;
        for (int i = 0; i < NUM_PILES; i++) {
            int end = start + numCardsPerPile;
            if (numCardsLeft > 0) {
                end++;
                numCardsLeft--;
            }
            ArrayList<ICard> pileCards = new ArrayList<>(deck.subList(start, end));
            IPile pile = createPile(pileCards);
            piles.add(pile);
            start = end;
        }

        return piles;
    }

    @Override
    protected IMarket createMarket(ArrayList<IPile> piles) throws MarketGenerationException {
        if (piles.size() != NUM_PILES) {
            throw new MarketGenerationException("Incorrect number of piles.");
        }
        ICard[][] marketLayout = new ICard[MARKET_ROWS][MARKET_COLS];
        for (int i = 0; i < piles.size(); i++) {
            // Draw two cards from each pile (to the column below the respective pile)
            marketLayout[i][0] = piles.get(i).drawTop();
            if(marketLayout[i][0].isCriteriaSideActive()) marketLayout[i][0].flip();
            marketLayout[i][1] = piles.get(i).drawTop();
            if(marketLayout[i][1].isCriteriaSideActive()) marketLayout[i][1].flip();
        }
        return new PointSaladMarket(marketLayout);
    }

    @Override
    protected IPile createPile(ArrayList<ICard> cards) {
        return new PointSaladPile(cards);
    }

    @Override
    protected ICard createCard(JSONObject cardJson){
        ICriteriaStrategy criteriaStrategy = createCriteria(cardJson.getJSONObject("criteria"));
        IResource resource = createResource(cardJson.getJSONObject("resource"));
        return new PointSaladCard(criteriaStrategy, resource);
    }

    @Override
    protected ICriteriaStrategy createCriteria(JSONObject criteriaJson){
        // Match criteria string to criteria class with given parameters
        String name = criteriaJson.getString("Name");
        JSONArray pointsArray = criteriaJson.getJSONArray("Points");
        JSONArray argsArray = criteriaJson.getJSONArray("Args");

        switch (name) {
            case "PointsIfHasMostOfResource": {
                int points = pointsArray.getInt(0);
                String resourceString = argsArray.getString(0);
                IResource resource = PointSaladResource.valueOf(resourceString);
                return new PointsIfHasMostOrFewestOfResource(resource, points, true);
                }
            case "PointsIfHasFewestOfResource": {
                int points = pointsArray.getInt(0);
                String resourceString = argsArray.getString(0);
                IResource resource = PointSaladResource.valueOf(resourceString);
                return new PointsIfHasMostOrFewestOfResource(resource, points, false);
                }
            case "PointsIfHasEvenOrOddOfResource": {
                int evenPoints = pointsArray.getInt(0);
                int oddPoints = pointsArray.getInt(1);
                String resourceString = argsArray.getString(0);
                IResource resource = PointSaladResource.valueOf(resourceString);
                return new PointsIfHasEvenOrOddOfResource
                        (resource, evenPoints, oddPoints, true);
                }
            case "PointsPerCopyOfResource": {
                ArrayList<Integer> points = new ArrayList<>();
                ArrayList<IResource> resources = new ArrayList<>();
                for (int i = 0; i < pointsArray.length(); i++) {
                    points.add(pointsArray.getInt(i));
                    String resourceString = argsArray.getString(i);
                    IResource resource = PointSaladResource.valueOf(resourceString);
                    resources.add(resource);
                }
                return new PointsPerCopyOfOneOrManyResources(points, resources);
                }
            case "PointsPerCombinationOfResources": {
                int points = pointsArray.getInt(0);
                ArrayList<IResource> resources = new ArrayList<>();
                for (int i = 0; i < argsArray.length(); i++) {
                    String resourceString = argsArray.getString(i);
                    IResource resource = PointSaladResource.valueOf(resourceString);
                    resources.add(resource);
                }
                return new PointsPerCombinationOfResources(points, resources);
                }
            case "PointsIfMostTotalResources": {
                int points = pointsArray.getInt(0);
                return new PointsIfMostOrFewestTotalResources(points, true);
                }
            case "PointsIfFewestTotalResources": {
                int points = pointsArray.getInt(0);
                return new PointsIfMostOrFewestTotalResources(points, false);
                }
            case "PointsPerTypeIfHasAtLeastXOfType": {
                int points = pointsArray.getInt(0);
                int minOfType = argsArray.getInt(0);
                return new PointsPerTypeIfHasAtLeastXOfType(points, minOfType);
                }
            case "PointsPerMissingType": {
                int points = pointsArray.getInt(0);
                return new PointsPerMissingType(points, NUM_RESOURCES);
                }
            case "PointsIfHasCompleteSet": {
                int points = pointsArray.getInt(0);
                return new PointsIfHasCompleteSet(points, NUM_RESOURCES);
            }
            default:
                throw new IllegalArgumentException("Unknown criteria: " + name);
        }
    }

    @Override
    protected IResource createResource(JSONObject resourceJson){
        String name = resourceJson.getString("Name");
        return PointSaladResource.valueOf(name);
    }

    private Map<IResource, ArrayList<ICard>> sortCardsByResource(ArrayList<ICard> cards) {
        Map<IResource, ArrayList<ICard>> sortedCards = new HashMap<>();

        for (ICard card : cards) {
            IResource resource = card.getResource();
            sortedCards.get(resource).add(card);
        }

        return sortedCards;
    }
}
