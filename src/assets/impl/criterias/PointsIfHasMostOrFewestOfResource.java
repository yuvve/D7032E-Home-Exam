package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;

/**
 * The calculation algorithm for the condition "Points if the player has the most or fewest of a resource"
 *  (most or fewest copies of the given resource compared to all other players)
 */
public class PointsIfHasMostOrFewestOfResource implements ICriteriaStrategy {
    private final IResource resource;
    private final int pointsValue;
    private final boolean mostOf; // if false then fewest of

    /**
     * Constructor for PointsIfHasMostOrFewestOfResource
     * @param resource The resource to check for
     * @param pointsValue The points to award if the player has the most or fewest of the resource
     * @param mostOf If true, the player must have the most of the resource to earn points
     *               If false, the player must have the fewest of the resource to earn points
     */
    public PointsIfHasMostOrFewestOfResource(IResource resource, int pointsValue, boolean mostOf) {
        this.resource = resource;
        this.pointsValue = pointsValue;
        this.mostOf = mostOf;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        int playerCount = 0;
        int bestCount = (mostOf ? 0 : Integer.MAX_VALUE);
        int points = 0;

        for (ArrayList<IResource> otherPlayerResources : otherPlayersResources) {
            int count = Util.countResourceOccurrences(resource, otherPlayerResources);
            if (mostOf) {
                if (count > bestCount) {
                    bestCount = count;
                }
            } else {
                if (count < bestCount) {
                    bestCount = count;
                }
            }
        }
        playerCount = Util.countResourceOccurrences(resource, playerResources);
        if ((mostOf && playerCount >= bestCount) || (!mostOf && playerCount <= bestCount)) {
            points = pointsValue;
        }
        return points;
    }

    @Override
    public String represent() {
        return "\"" + pointsValue + " points if you have the "
                + (mostOf ? "most" : "fewest") + " " + resource.represent() + ".\"";
    }
}
