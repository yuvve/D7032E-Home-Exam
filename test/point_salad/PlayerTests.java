package point_salad;

import common.point_salad.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import player.IAbstractPlayerAssetsFactory;
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
    private static IAbstractPlayerAssetsFactory playerAssetsFactory;

    @BeforeAll
    public static void setUpAll() {
        random = new Random();
        playerAssetsFactory = new PointSaladPlayerAssetsFactory(random);
    }

    /**
     * <H1>Requirement 1</H1>
     * Test that the player manager can be created with the correct number of players.
     * This test checks that the player manager can be created with the minimum and maximum number of players.
     * It also checks that the player manager cannot be created with less than the minimum number of players
     * or more than the maximum number of players.
     */
    @Test
    public void testNumberOfPlayers(){
        try {
            playerAssetsFactory.createPlayerManager(MIN_PLAYERS - 1, 0);
            fail("Should not be able to create a player manager with less than the minimum number of players");
        } catch (IllegalArgumentException e) {
            assert(true);
        }
        try {
            playerAssetsFactory.createPlayerManager(MAX_PLAYERS + 1, 0);
            fail("Should not be able to create a player manager with more than the maximum number of players");
        } catch (IllegalArgumentException e) {
            assert(true);
        }
        try {
            playerAssetsFactory.createPlayerManager(MIN_PLAYERS, 0);
            assert(true);
        } catch (IllegalArgumentException e) {
            fail("Should be able to create a player manager with the minimum number of players");
        }
        try {
            playerAssetsFactory.createPlayerManager(MAX_PLAYERS, 0);
            assert(true);
        } catch (IllegalArgumentException e) {
            fail("Should be able to create a player manager with the maximum number of players");
        }
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
