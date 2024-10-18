package point_salad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.IPlayerManager;
import player.IPlayerManagerFactory;
import player.impl.PointSaladPlayerManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 6;

    /**
     * <H1>Requirement 6</H1>
     * Test that a random starting player is chosen every time a new game is started.
     * This test is probabilistic and may fail even if the implementation is correct.
     */
    @Test
    public void testRandomStartingPlayer() {
        // A VERY simple probabilistic test to check that the starting player is random

        int totalExperiments = 100;
        int lastStartingPlayerId = -1;
        boolean foundDifferentStartingPlayers = false;
        for (int i = 0; i< totalExperiments; i++){
            IPlayerManagerFactory playerManagerFactory = new PointSaladPlayerManagerFactory();
            IPlayerManager playerManager = playerManagerFactory.createPlayerManager(MAX_PLAYERS, 0);

            int startingPlayerId = playerManager.getCurrentPlayer().getPlayerId();
            if (lastStartingPlayerId == -1){
                lastStartingPlayerId = startingPlayerId;
            } else if (lastStartingPlayerId != startingPlayerId){
                foundDifferentStartingPlayers = true;
                break;
            }
        }
        assertTrue(foundDifferentStartingPlayers, "Starting player is most likely not random");
    }
}
