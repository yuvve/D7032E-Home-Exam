package assets;

import common.IRepresentable;

/**
 * Interface for what is considered in the game-domain a resource (objects that generate points).
 */
public interface IResource extends IRepresentable {
    /**
     * Gets the type (in the game-domain sense) of the resource.
     * @return The (game-domain) type of the resource.
     */
    String getType();

    /**
     * Checks if this resource is equal to another object.
     * We want to make sure that no matter how a resource is implemented,
     * we can compare it with another resource in the game-domain sense
     * and not necessarily in the object reference sense.
     * @param obj The object to compare with.
     * @return Whether the objects are equal.
     */
    boolean equals(Object obj);
}
