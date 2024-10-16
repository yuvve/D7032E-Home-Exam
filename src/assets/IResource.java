package assets;

import common.IRepresentable;

/// <summary>
/// Interface for what is considered in the game-domain a resource (objects that generate points).
/// </summary>
public interface IResource extends IRepresentable {
    String getType();
}
