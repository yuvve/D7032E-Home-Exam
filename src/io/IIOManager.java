package io;

/**
 * Abstracts input and output operations for the game.
 */
public interface IIOManager {

    /**
     * Register a player with the input/output system.
     * @param playerId the id of the player
     */
    void registerPlayer(int playerId);

    /**
     * Get the input from the player.
     * @param Playerid the id of the player
     * @return the input from the player
     */
    String getPlayerInput(int Playerid);

    /**
     * Send a message to the player.
     * @param Playerid the id of the player
     * @param msg the message to send
     */
    void sendMsg(int Playerid, String msg);

    /**
     * Send a message to all players.
     * @param msg the message to send
     */
    void broadcast(String msg);

    /**
     * Send game over signals to all players.
     */
    void endGame();
}
