package point_salad;


import assets.*;
import assets.impl.PointSaladAssetsFactory;
import assets.impl.PointSaladCard;
import assets.impl.PointSaladGameBoard;
import assets.impl.PointSaladResource;
import assets.impl.criterias.PointsIfMostOrFewestTotalResources;
import common.point_salad.Constants;
import common.point_salad.ManifestMetadata;
import game.ITurnActionStrategy;
import game.Util;
import game.impl.turns.PointSaladHumanFree;
import game.impl.turns.PointSaladHumanMain;
import io.IIOManager;
import mocking.MockIO;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import player.IAbstractPlayerAssetsFactory;
import player.IPlayer;
import player.IPlayerManager;
import player.impl.PointSaladPlayerAssetsFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTests {
    private static IAbstractAssetsFactory assetsFactory;
    private static IAbstractPlayerAssetsFactory playerAssetsFactory;
    private static JSONObject deckJson;
    private static mocking.Random random;
    private static IGameBoard gameBoard;
    private static IMarket market;
    private static ArrayList<IPile> piles;
    private static IPlayerManager playerManager;

    @BeforeAll
    public static void setUpAll(){
        try {
            deckJson = Util.fileToJSON(ManifestMetadata.MANIFEST_FILENAME.getValue());
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find the deck manifest.");
        }
    }

    @AfterAll
    public static void tearDownAll(){
        deckJson = null;
    }

    @BeforeEach
    public void setUp() {
        random = new mocking.Random();

        assetsFactory = new PointSaladAssetsFactory(random);
        playerAssetsFactory = new PointSaladPlayerAssetsFactory(random);

        ArrayList<ICard> deck = assetsFactory.createDeck(deckJson, Constants.MAX_PLAYERS.getValue());
        piles = assetsFactory.createPiles(deck);
        market = assetsFactory.createMarket(piles);
        gameBoard = new PointSaladGameBoard(piles, market);

        playerManager = playerAssetsFactory.createPlayerManager(
                1,Constants.MAX_PLAYERS.getValue()-1);
    }

    @AfterEach
    public void tearDown() {
        random = null;

        assetsFactory = null;
        playerAssetsFactory = null;

        piles = null;
        market = null;
        gameBoard = null;
        playerManager = null;
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
        Map<Integer, ArrayList<String>> playerToActions = new HashMap<>();
        ArrayList<String> actions = new ArrayList<>();
        actions.add("M00");
        actions.add("M0011");
        actions.add("P1");
        playerToActions.put(0, actions);

        IIOManager io = new MockIO(playerToActions);
        ITurnActionStrategy mainAction = new PointSaladHumanMain(gameBoard, io);

        IPlayer humanPlayer = playerManager.getPlayerById(0);

        assertTrue(humanPlayer.getHand().isEmpty(), "Player hand should be empty at the start of the game.");

        mainAction.executeTurnAction(humanPlayer);
        assertEquals(1, humanPlayer.getResourceCards().size(),
                "Player should have drawn a card from the market.");

        gameBoard.refillMarket();

        mainAction.executeTurnAction(humanPlayer);
        assertEquals(3, humanPlayer.getResourceCards().size(),
                "Player should have drawn two cards from the market.");

        gameBoard.refillMarket();

        mainAction.executeTurnAction(humanPlayer);
        assertEquals(1, humanPlayer.getCriteriaCards().size(),
                "Player should have drawn a card from the piles.");
    }

    /**
     * <H1>Requirement 7</H1>
     * Test that the player can take their free action:
     * Flip a criteria card
     */
    @Test
    public void testPlayerFreeAction(){
        Map<Integer, ArrayList<String>> playerToActions = new HashMap<>();
        ArrayList<String> actions = new ArrayList<>();
        actions.add("P1");
        actions.add("0");
        playerToActions.put(0, actions);

        IIOManager io = new MockIO(playerToActions);
        ITurnActionStrategy mainAction = new PointSaladHumanMain(gameBoard, io);
        ITurnActionStrategy freeAction = new PointSaladHumanFree(gameBoard, io);

        IPlayer humanPlayer = playerManager.getPlayerById(0);

        assertTrue(humanPlayer.getHand().isEmpty(), "Player hand should be empty at the start of the game.");

        mainAction.executeTurnAction(humanPlayer);
        assertEquals(0,humanPlayer.getResourceCards().size(),
                "Player should not have any resource cards in their hand.");
        assertEquals(1,humanPlayer.getCriteriaCards().size(),
                "Player should have drawn a criteria card.");

        freeAction.executeTurnAction(humanPlayer);
        assertEquals(0,humanPlayer.getCriteriaCards().size(),
                "Player should have flipped their criteria card.");
        assertEquals(1,humanPlayer.getResourceCards().size(),
                "Player should have flipped their criteria card.");
    }

    /**
     * <H1>Requirement 9</H1>
     * Test that the players' hands are being shown to each other
     */
    @Test
    public void testHandsAreShown(){
        IPlayer humanPlayer = playerManager.getPlayerById(0);
        IPlayer player1 = playerManager.getPlayerById(1);

        IResource resource = PointSaladResource.CABBAGE;
        ICriteriaStrategy criteria = new PointsIfMostOrFewestTotalResources(10, true);
        ICard card1 = new PointSaladCard(criteria, resource);
        ICard card2 = new PointSaladCard(criteria, resource);
        ICard card3 = new PointSaladCard(criteria, resource);
        card1.flip();
        card2.flip();

        player1.addToHand(card1);
        player1.addToHand(card2);
        player1.addToHand(card3);

        String playersRepresentation = playerManager.represent();
        String expectedResources =
               """
               |CAB|
               | 2 |
               """;
        String expectedCriteria = "points if you have the most total resources.";

        assertTrue(playersRepresentation.contains(expectedResources),
                "Player 1's resources should be shown to the other player.");
        assertTrue(playersRepresentation.contains(expectedCriteria),
                "Player 1's criteria should be shown to the other player.");
    }

    /**
     * <H1>Requirement 12</H1>
     * Test that the game continues (each player gets their turn) until the game ends
     */
}
