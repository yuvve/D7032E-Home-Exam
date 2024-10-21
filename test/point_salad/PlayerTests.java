package point_salad;

import common.point_salad.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.IPlayer;
import player.IPlayerManager;
import player.impl.PointSaladPlayer;
import player.impl.PointSaladPlayerAssetsFactory;
import player.impl.PointSaladPlayerManager;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
    private static final int MIN_PLAYERS = Constants.MIN_PLAYERS.getValue();
    private static final int MAX_PLAYERS = Constants.MAX_PLAYERS.getValue();
    private static Random random;

    @BeforeAll
    public static void setUpAll() {
        random = new Random();
    }

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
            IPlayerManager playerManager = new PointSaladPlayerAssetsFactory(random).
                    createPlayerManager(MAX_PLAYERS,0);

            int startingPlayerId = playerManager.getCurrentPlayer().getId();
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
