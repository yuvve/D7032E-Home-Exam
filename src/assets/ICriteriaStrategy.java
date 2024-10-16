package assets;

import common.IRepresentable;

import java.util.ArrayList;

/// <summary>
/// Interface representing scoring criteria logic.
/// </summary>
public interface ICriteriaStrategy extends IRepresentable {
    int calcScore(
            ArrayList<IResource> playerResources,
            ArrayList<ArrayList<IResource>> otherPlayersResources
    );
}
