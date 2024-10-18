package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;

public class PointsIfHasCompleteSet implements ICriteriaStrategy {
    private final int pointsValue;
    private final int totalNrTypes;

    /**
     * Constructor for PointsIfHasCompleteSet
     * @param pointsValue the number of points to be awarded if the player has a complete set
     * @param totalNrTypes the total number of types of resources in the game
     */
    public PointsIfHasCompleteSet(int pointsValue, int totalNrTypes) {
        this.pointsValue = pointsValue;
        this.totalNrTypes = totalNrTypes;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        int points = 0;
        int nrUniqueTypes = Util.countUniqueTypes(playerResources);
        if (nrUniqueTypes == totalNrTypes) {
            points = pointsValue;
        }

        return points;
    }

    @Override
    public String represent() {
        return "Get " + pointsValue + " points if you have at least one of every resource.";
    }
}
