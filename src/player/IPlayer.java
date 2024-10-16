package player;

import assets.ICard;
import common.IRepresentable;

import java.util.ArrayList;

/// <summary>
/// Interface for a game-domain player (not a multiplayer entity).
/// </summary>
public interface IPlayer extends IRepresentable {
    int getPlayerId();
    void setPlayerId(int playerId);
    boolean isBot();
    ArrayList<ICard> getHand();
    void discard(ICard card);
    void addToHand(ICard card);
    void flipCard(ICard card);
}
