package player;

/**
 * Interface for creating player managers.
 */
public interface IPlayerManagerFactory {

        /**
         * Create a player manager.
         * @param numHumans the number of human players
         *                  Must be between 1 and 6 players
         * @param numBots the number of bot players
         *                Must be between 0 and 5 players
         * @return the player manager
         */
        IPlayerManager createPlayerManager(int numHumans, int numBots);
}
