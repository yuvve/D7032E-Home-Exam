package player;


/**
 * Interface for creating players and player managers.
 */
public interface IAbstractPlayerAssetsFactory {
    /**
     * Create a player.
     * @param isBot true if the player is a bot, false otherwise
     * @return the player
     */
    IPlayer createPlayer(boolean isBot);

    /**
     * Create a player manager.
     * @param numHumanPlayers the number of human players
     * @param numBotPlayers the number of bot players
     * @return the player manager
     */
    IPlayerManager createPlayerManager(int numHumanPlayers, int numBotPlayers);
}
