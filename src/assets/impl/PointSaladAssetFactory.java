package assets.impl;

import assets.*;
import exceptions.IncorrectDeckForPileGeneration;
import exceptions.IncorrectPilesForMarketGeneration;
import org.json.JSONObject;

import java.util.ArrayList;

public class PointSaladAssetFactory implements IAbstractAssetFactory {
    @Override
    public IGameBoard createGameBoard(JSONObject gameBoardJson) throws IncorrectDeckForPileGeneration, IncorrectPilesForMarketGeneration {
        return null;
    }

    @Override
    public ArrayList<IPile> createPiles(ArrayList<ICard> deck) throws IncorrectDeckForPileGeneration {
        return null;
    }

    @Override
    public IMarket createMarket(ArrayList<IPile> piles) throws IncorrectPilesForMarketGeneration {
        return null;
    }

    @Override
    public IPile createPile(ArrayList<ICard> cards) {
        return null;
    }

    @Override
    public ICard createCard(JSONObject cardJson) {
        return null;
    }

    @Override
    public ICriteriaStrategy createCriteria(JSONObject criteriaJson) {
        return null;
    }

    @Override
    public IResource createResource(JSONObject resourceJson) {
        return null;
    }
}
