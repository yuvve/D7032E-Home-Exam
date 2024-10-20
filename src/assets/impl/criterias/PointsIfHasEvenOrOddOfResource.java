package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;

public class PointsIfHasEvenOrOddOfResource implements ICriteriaStrategy {
    private final IResource resource;
    private final int pointsValue;
    private final boolean even; // if false then odd

    /**
     * Constructor for PointsIfHasEvenOrOddOfResource
     * @param resource The resource to check for
     * @param evenPointsValue The points to award if the player has an even number of the resource
     * @param oddPointsValue The points to award if the player has an odd number of the resource
     * @param even If true, the player must have an even number of the resource to earn points
     *             If false, the player must have an odd number of the resource to earn points
     */
    public PointsIfHasEvenOrOddOfResource(
            IResource resource, int evenPointsValue,
            int oddPointsValue, boolean even) {
        this.resource = resource;
        this.pointsValue = evenPointsValue;
        this.even = even;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        int playerCount = 0;
        int points = 0;

        playerCount = Util.countResourceOccurrences(resource, playerResources);

        if (playerCount % 2 == 0 && even) {
            if (playerCount != 0) { // No points awarded if player has 0 of the resource
                points = pointsValue;
            }
        } else if (playerCount % 2 != 0 && !even) {
            points = pointsValue;
        }
        return points;
    }

    @Override
    public String represent() {
        return "\"" + pointsValue + " points if you have an "
                + (even ? "even" : "odd") + " number of " + resource.represent() + ".\"";
    }
}
