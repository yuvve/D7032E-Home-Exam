package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;

public class PointsPerCombinationOfResource implements ICriteriaStrategy {
    private final int pointsValue;
    private final ArrayList<IResource> resourcesInCombo;

    /**
     * Constructor for PointsPerCombinationOfResource
     * @param pointsValue The points to award for each combination of resources
     * @param resourcesInCombo The resources that must be in a combination to earn points
     */
    public PointsPerCombinationOfResource(int pointsValue, ArrayList<IResource> resourcesInCombo) {
        this.pointsValue = pointsValue;
        this.resourcesInCombo = resourcesInCombo;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        ArrayList<Integer> occurrencesCount = new ArrayList<>();
        for (IResource resource : resourcesInCombo) {
            occurrencesCount.add(Util.countResourceOccurrences(resource, playerResources));
        }

        occurrencesCount.sort(Integer::compareTo);
        int minOccurrences = occurrencesCount.getFirst();

        return minOccurrences * pointsValue;
    }

    @Override
    public String represent() {
        StringBuilder resourcesString = new StringBuilder();
        for (IResource resource : resourcesInCombo) {
            resourcesString.append(resource.represent()).append(" and ");
        }
        resourcesString.delete(resourcesString.length() - 5, resourcesString.length());

        return "Get " + pointsValue + " points for each combination of " + resourcesString;
    }
}
