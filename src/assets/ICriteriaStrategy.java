package assets;

import common.IRepresentable;

import java.util.ArrayList;

/**
 * Interface for a scoring criteria strategy.
 */
public interface ICriteriaStrategy extends IRepresentable {
    /**
     * Calculates the score of a player based on their resources and the resources of other players.
     * @param playerResources The player's resources.
     * @param otherPlayersResources The resources of the other players.
     * @return The player's score.
     */
    int calcScore(
            ArrayList<IResource> playerResources,
            ArrayList<ArrayList<IResource>> otherPlayersResources
    );
}
