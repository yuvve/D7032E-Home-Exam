package game;

import assets.IGameBoard;
import player.IPlayerManager;

/// <summary>
/// Interface for game-wide logic (e.g. game end conditions, scoring).
/// </summary>
public interface IGameRules {
    boolean hasGameEnded(IGameBoard gameBoard);
    int[] calculateScores(IPlayerManager playerManager);
}
