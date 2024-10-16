package assets;

import common.IRepresentable;

import java.util.ArrayList;

/// <summary>
/// Interface for what is considered in the game-domain a game board.
/// Contains the card pile(s) and the market cards.
/// </summary>
public interface IGameBoard extends IRepresentable {
    ArrayList<IPile> getPiles();
    ICard[][] getMarketCards();
    ICard draftCard(int row, int column);
    void drawCardIntoMarket(int row, int column);
}
