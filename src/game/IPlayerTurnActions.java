package game;

import assets.IGameBoard;
import player.IPlayerManager;

/// <summary>
/// Interface for the possible actions a player can do in their turn.
/// </summary>
public interface IPlayerTurnActions {
    void performMainAction(IPlayerManager playerManager, IGameBoard gameBoard);
    void performBonusAction(IPlayerManager playerManager, IGameBoard gameBoard);
}
