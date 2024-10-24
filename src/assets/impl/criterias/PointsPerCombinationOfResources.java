package assets.impl.criterias;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The calculation algorithm for the condition "Points per combination of resources",
 *  which can be any number of resources > 2
 */
public class PointsPerCombinationOfResources implements ICriteriaStrategy {
    private final int pointsValue;
    private final ArrayList<IResource> resourcesInCombo;

    /**
     * Constructor for PointsPerCombinationOfResource
     * @param pointsValue The points to award for each combination of resources
     * @param resourcesInCombo The resources that must be in a combination to earn points
     */
    public PointsPerCombinationOfResources(int pointsValue, ArrayList<IResource> resourcesInCombo) {
        this.pointsValue = pointsValue;
        this.resourcesInCombo = resourcesInCombo;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        ArrayList<Integer> compensatedOccurrencesCount = new ArrayList<>();

        // Must account for duplicate resources in the combo
        Map<IResource, Integer> repeatedResourcesInCombo = new HashMap<>();
        for (IResource resource : resourcesInCombo) {
            if (repeatedResourcesInCombo.containsKey(resource)) {
                repeatedResourcesInCombo.put(resource, repeatedResourcesInCombo.get(resource) + 1);
            } else {
                repeatedResourcesInCombo.put(resource, 1);
            }
        }

        for (IResource resource : resourcesInCombo) {
            int resourceOccurrences = Util.countResourceOccurrences(resource, playerResources);
            compensatedOccurrencesCount.add(resourceOccurrences / repeatedResourcesInCombo.get(resource));
        }

        compensatedOccurrencesCount.sort(Integer::compareTo);
        int minOccurrences = compensatedOccurrencesCount.getFirst();

        return minOccurrences * pointsValue;
    }

    @Override
    public String represent() {
        StringBuilder resourcesString = new StringBuilder();
        for (IResource resource : resourcesInCombo) {
            resourcesString.append(resource.represent()).append(" and ");
        }
        resourcesString.delete(resourcesString.length() - 5, resourcesString.length());

        return "\"" + pointsValue + " points for each combination of " + resourcesString + ".\"";
    }
}
