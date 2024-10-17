package assets;

import java.util.ArrayList;

/**
 * Utility class for common methods.
 */
public class Util {
    /**
     * Counts the number of occurrences of a resource in a list of resources.
     * @param compareWith The resource to compare with.
     * @param resources The list of resources.
     * @return The number of occurrences.
     */
    public static int countResourceOccurrences(IResource compareWith, ArrayList<IResource> resources) {
        int count = 0;
        for (IResource playerResource : resources) {
            if (playerResource == compareWith) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of unique types of resources in a list of resources.
     * @param resources The list of resources.
     * @return The number of unique types.
     */
    public static int countUniqueTypes(ArrayList<IResource> resources){
        int uniqueCount = 0;
        ArrayList<IResource> seenTypes = new ArrayList<>();
        for (IResource resource : resources) {
            if (!seenTypes.contains(resource)) {
                seenTypes.add(resource);
                uniqueCount++;
            }
        }
        return uniqueCount;
    }

    /**
     * Counts the number of occurrences of each unique type of resource in a list of resources.
     * @param resources The list of resources.
     * @return The number of occurrences of each unique type (without any special meaning for the order).
     */
    public static ArrayList<Integer> countUniqueTypeOccurrences(ArrayList<IResource> resources){
        ArrayList<Integer> uniqueTypeOccurrences = new ArrayList<>();
        ArrayList<IResource> seenTypes = new ArrayList<>();
        for (IResource resource : resources) {
            if (!seenTypes.contains(resource)) {
                seenTypes.add(resource);
                uniqueTypeOccurrences.add(1);
            } else {
                int index = seenTypes.indexOf(resource);
                uniqueTypeOccurrences.set(index, uniqueTypeOccurrences.get(index) + 1);
            }
        }
        return uniqueTypeOccurrences;
    }
}
