package assets.impl;

import assets.*;
import assets.impl.criterias.*;
import common.point_salad.Constants;
import common.point_salad.ManifestMetadata;
import exceptions.DeckGenerationException;
import exceptions.PileGenerationException;
import exceptions.MarketGenerationException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class PointSaladAssetsFactory implements IAbstractAssetsFactory {
    private static final int MIN_PLAYERS = Constants.MIN_PLAYERS.getValue();
    private static final int MAX_PLAYERS = Constants.MAX_PLAYERS.getValue();
    private static final int CARDS_TO_REMOVE_PER_PLAYER_MISSING =
            Constants.CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG.getValue();
    private static final int NUM_PILES = Constants.NUM_PILES.getValue();
    private static final int DECK_SIZE = Constants.DECK_SIZE.getValue();
    private static final int MARKET_ROWS = Constants.MARKET_ROWS.getValue();
    private static final int MARKET_COLS = Constants.MARKET_COLS.getValue();
    private static final int NUM_RESOURCES = Constants.NUM_TYPES.getValue();
    private static final String CARDS_FIELD = ManifestMetadata.CARDS_FIELD.getValue();
    private static final String ARGS_FIELD = ManifestMetadata.ARGS_FIELD.getValue();
    private static final String POINTS_FIELD = ManifestMetadata.POINTS_FIELD.getValue();
    private static final String NAME_FIELD = ManifestMetadata.NAME_FIELD.getValue();

    @Override
    public IGameBoard createGameBoard(JSONObject deckJson, int numPlayers)
            throws PileGenerationException, MarketGenerationException {

        ArrayList<ICard> deckCards = createDeck(deckJson, numPlayers);
        ArrayList<IPile> piles = createPiles(deckCards);
        IMarket market = createMarket(piles);

        return new PointSaladGameBoard(piles, market);
    }

    @Override
    public ArrayList<ICard> createDeck(JSONObject deckJson, int numPlayers)
            throws DeckGenerationException {

        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new DeckGenerationException("Invalid number of players.");
        }

        ArrayList<ICard> cards = new ArrayList<>();

        JSONArray cardsArray = deckJson.getJSONArray(CARDS_FIELD);
        for (int i = 0; i < cardsArray.length(); i++) {
            JSONObject cardJson = cardsArray.getJSONObject(i);
            ICard card = createCard(cardJson);
            cards.add(card);
        }

        if (cards.size() != DECK_SIZE) {
            throw new DeckGenerationException("Incorrect number of cards in deck.");
        }

        if (numPlayers == MAX_PLAYERS) {
            Collections.shuffle(cards);
            return cards;
        }

        Map<IResource, ArrayList<ICard>> resourcePiles = sortCardsByResource(cards);
        for (ArrayList<ICard> cardsPile: resourcePiles.values()) {
            Collections.shuffle(cardsPile);
            int numCardsToRemove = CARDS_TO_REMOVE_PER_PLAYER_MISSING * (MAX_PLAYERS - numPlayers);
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
    public ArrayList<IPile> createPiles(ArrayList<ICard> deck) throws PileGenerationException {
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
    public IMarket createMarket(ArrayList<IPile> piles) throws MarketGenerationException {
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
    public IPile createPile(ArrayList<ICard> cards) {
        return new PointSaladPile(cards);
    }

    @Override
    public ICard createCard(JSONObject cardJson){
        ICriteriaStrategy criteriaStrategy = createCriteria(cardJson.getJSONObject("Criteria"));
        IResource resource = createResource(cardJson.getString("Resource"));
        return new PointSaladCard(criteriaStrategy, resource);
    }

    @Override
    public ICriteriaStrategy createCriteria(JSONObject criteriaJson){
        // Match criteria string to criteria class with given parameters
        String name = criteriaJson.getString(NAME_FIELD);
        JSONArray pointsArray = criteriaJson.getJSONArray(POINTS_FIELD);
        JSONArray argsArray = criteriaJson.getJSONArray(ARGS_FIELD);

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
    public IResource createResource(String resourceName){
        return PointSaladResource.valueOf(resourceName);
    }

    private Map<IResource, ArrayList<ICard>> sortCardsByResource(ArrayList<ICard> cards) {
        Map<IResource, ArrayList<ICard>> sortedCards = new HashMap<>();

        for (ICard card : cards) {
            IResource resource = card.getResource();
            sortedCards.computeIfAbsent(resource, k -> new ArrayList<>()).add(card);        }

        return sortedCards;
    }
}
