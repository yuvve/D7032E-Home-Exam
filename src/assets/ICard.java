package assets;

import common.IRepresentable;

/// <summary>
/// Interface representing a card in the game, where one side is a scoring criteria and the other side is a resource.
/// </summary>
public interface ICard extends IRepresentable {
    ICriteriaStrategy getCriteriaStrategy();
    IResource getResource();
    Boolean isCriteriaSideActive();
    void flipCriteriaSide();
}
