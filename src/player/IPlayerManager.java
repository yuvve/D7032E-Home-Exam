package player;

import java.util.ArrayList;

/// <summary>
/// Interface for managing player order and turns.
/// </summary>
public interface IPlayerManager {
    ArrayList<IPlayer> getPlayers();
    IPlayer getCurrentPlayer();
    IPlayer getNextPlayer();
}
