package assets;

import org.json.JSONObject;

import java.util.ArrayList;

/// <summary>
/// Interface for an abstract factory that generates assets.
/// </summary>
public interface IAbstractAssetFactory {
    ArrayList<ICard> generateDeck(JSONObject deckJson);
    ICard createCard(JSONObject cardJson);
    ICriteriaStrategy createCriteria(JSONObject criteriaStrategyJson);
    IResource createResource(JSONObject resourceJson);
}
