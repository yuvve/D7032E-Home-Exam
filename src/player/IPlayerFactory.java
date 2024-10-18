package player;

/**
 * Interface for creating players.
 */
public interface IPlayerFactory {

    /**
     * Create a player.
     * @param isBot true if the player is a bot, false otherwise
     * @return the player
     */
    IPlayer createPlayer(boolean isBot);
}
