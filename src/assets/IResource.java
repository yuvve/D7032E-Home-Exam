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
}
