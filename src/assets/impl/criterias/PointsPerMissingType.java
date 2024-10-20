package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;

public class PointsPerMissingType implements ICriteriaStrategy {
    private final int pointsValue;
    private final int totalNrTypes;

    /**
     * Constructor for PointsPerMissingType
     * @param pointsValue the number of points to be awarded per missing type
     * @param totalNrTypes the total number of types of resources in the game
     */
    public PointsPerMissingType(int pointsValue, int totalNrTypes) {
        this.pointsValue = pointsValue;
        this.totalNrTypes = totalNrTypes;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        int nrUniqueTypes = Util.countUniqueTypes(playerResources);
        return (totalNrTypes - nrUniqueTypes) * pointsValue;
    }

    @Override
    public String represent() {
        return "\"" + pointsValue + " points for each missing resource type.\"";
    }
}
