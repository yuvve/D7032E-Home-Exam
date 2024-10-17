package assets.impl;

import assets.ICriteriaStrategy;
import assets.IResource;
import assets.Util;

import java.util.ArrayList;

public class PointsPerCopyOfOneOrManyResources implements ICriteriaStrategy {
    private final ArrayList<Integer> pointsValue;
    private final ArrayList<IResource> resources;

    /**
     * Constructor for PointsPerCopyOfOneOrManyResources
     * @param pointsValue The points to award for each copy of the resource at the same index
     * @param resources The resources to award points for
     * @throws IllegalArgumentException if the number of points values does not match the number of resources
     */
    public PointsPerCopyOfOneOrManyResources(
            ArrayList<Integer> pointsValue, ArrayList<IResource> resources
    ) throws IllegalArgumentException {
        if (pointsValue.size() != resources.size()) {
            throw new IllegalArgumentException("The number of points values must match the number of resources.");
        }
        this.pointsValue = pointsValue;
        this.resources = resources;
    }

    @Override
    public int calcScore(ArrayList<IResource> playerResources, ArrayList<ArrayList<IResource>> otherPlayersResources) {
        int points = 0;
        for (int i = 0; i < resources.size(); i++) {
            int playerCount = Util.countResourceOccurrences(resources.get(i), playerResources);
            points += playerCount * pointsValue.get(i);
        }
        return points;
    }

    @Override
    public String represent() {
        StringBuilder resourcesString = new StringBuilder();
        for (int i = 0; i < resources.size(); i++) {
            resourcesString.append(pointsValue.get(i)).append(" points for each ")
                    .append(resources.get(i).represent()).append(", ");
        }
        resourcesString.delete(resourcesString.length() - 2, resourcesString.length());

        return "Get " + resourcesString;
    }
}
