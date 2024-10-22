package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;

import java.util.ArrayList;

/**
 * The calculation algorithm for the condition "Points if the player has the most or fewest total resources"
 *  (most or fewest total resources compared to all other players)
 */
public class PointsIfMostOrFewestTotalResources implements ICriteriaStrategy {
    private final int pointsValue;
    private final boolean mostOf; // if false then fewest of

    /**
     * Constructor for PointsIfMostOrFewestTotalResources
     * @param pointsValue The points to award if the player has the most or fewest total resources
     * @param mostOf If true, the player must have the most total resources to earn points
     *               If false, the player must have the fewest total resources to earn points
     */
    public PointsIfMostOrFewestTotalResources(int pointsValue, boolean mostOf) {
        this.pointsValue = pointsValue;
        this.mostOf = mostOf;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        int playerCount = 0;
        int bestCount = (mostOf ? 0 : Integer.MAX_VALUE);
        int points = 0;

        for (ArrayList<IResource> otherPlayerResources : otherPlayersResources) {
            int count = otherPlayerResources.size();
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
        playerCount = playerResources.size();

        if ((mostOf && playerCount >= bestCount) || (!mostOf && playerCount <= bestCount)) {
            points = pointsValue;
        }
        return points;
    }

    @Override
    public String represent() {
        return "\"" + pointsValue + " points if you have the "
                + (mostOf ? "most" : "fewest") + " total resources.\"";
    }
}
