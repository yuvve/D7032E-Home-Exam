package point_salad;


import assets.IAbstractAssetsFactory;
import assets.IGameBoard;
import assets.impl.PointSaladAssetsFactory;
import common.point_salad.Constants;
import game.ITurnActionStrategy;
import game.Util;
import game.impl.turns.PointSaladHumanFree;
import game.impl.turns.PointSaladHumanMain;
import networking.IServer;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.IAbstractPlayerAssetsFactory;
import player.IPlayerManager;
import player.impl.PointSaladPlayerAssetsFactory;

import java.io.FileNotFoundException;
import java.util.Random;

public class GameTests {
    private static IAbstractAssetsFactory assetsFactory;
    private static IAbstractPlayerAssetsFactory playerAssetsFactory;
    private static JSONObject deckJson;
    private static Random random;
    private static IGameBoard gameBoard;

    private static IServer server;

    @BeforeAll
    public static void setUpAll() {
        assetsFactory = new PointSaladAssetsFactory(random);
        playerAssetsFactory = new PointSaladPlayerAssetsFactory(random);
        random = new Random();

        try {
            deckJson = Util.fileToJSON("PointSaladManifest.json");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find the deck manifest.");
        }
    }

    @BeforeEach
    public void setUp() {
        gameBoard = assetsFactory.createGameBoard(deckJson, Constants.MAX_PLAYERS.getValue());
    }


    /**
     * <H1>Requirement 7</H1>
     * Test that the player can take their main action:
     * - Draw a card from the market OR
     * - Draw two cards from the market OR
     * - Draw a card from one of the piles
     */
    @Test
    public void testPlayerMainAction(){

    }
}
