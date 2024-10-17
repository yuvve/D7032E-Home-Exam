package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;

public class PointsPerTypeIfHasAtLeastXOfType implements ICriteriaStrategy {
    private final int pointsValue;
    private final int acceptableLimit;

    /**
     * Constructor for PointsPerTypeIfHasAtLeastXOfType
     * @param pointsValue the number of points to be awarded per type of resource the player has at least acceptableLimit of
     * @param acceptableLimit the minimum number of resources of a type the player must have to earn points
     */
    public PointsPerTypeIfHasAtLeastXOfType(int pointsValue, int acceptableLimit) {
        this.pointsValue = pointsValue;
        this.acceptableLimit = acceptableLimit;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        int points = 0;
        ArrayList<Integer> uniqueTypeOccurrenceList = Util.countUniqueTypeOccurrences(playerResources);
        for (Integer uniqueTypeOccurrence : uniqueTypeOccurrenceList) {
            if (uniqueTypeOccurrence >= acceptableLimit) {
                points += pointsValue;
            }
        }
        return points;
    }

    @Override
    public String represent() {
        return "Get " + pointsValue + " points for each type of resource you have at least " + acceptableLimit + " of.";
    }
}
