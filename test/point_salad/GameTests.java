package point_salad;


import assets.*;
import assets.impl.PointSaladAssetsFactory;
import assets.impl.PointSaladCard;
import assets.impl.PointSaladGameBoard;
import assets.impl.PointSaladResource;
import assets.impl.criterias.PointsIfMostOrFewestTotalResources;
import assets.impl.criterias.PointsPerCopyOfOneOrManyResources;
import common.point_salad.Constants;
import common.point_salad.ManifestMetadata;
import game.GameLoopTemplate;
import game.ITurnActionStrategy;
import game.Util;
import game.impl.PointSaladGameLoop;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        for (int i=0; i < Constants.MAX_PLAYERS.getValue(); i++){
            io.registerPlayer(i);
        }
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
        for (int i=0; i < Constants.MAX_PLAYERS.getValue(); i++){
            io.registerPlayer(i);
        }
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
    @Test
    public void testPlayersTakeTurns(){
        IAbstractPlayerAssetsFactory playerAssetsFactory1 = new PointSaladPlayerAssetsFactory(random);
        IPlayerManager playerManager = playerAssetsFactory1.createPlayerManager(
                6, 0);
        MockIO io = runFakeMatch(playerManager);

        // Check that all players play every round
        ArrayList<Integer> searchOccurences = new ArrayList<>();

        for (int playerId=0; playerId<6; playerId++){
            ArrayList<String> msgs = io.getMessages(playerId);
            searchOccurences.add(0);
            String search = "It's your turn (player " + playerId + ")!";
            for (int msgId=0; msgId<msgs.size(); msgId++){
                if (msgs.get(msgId).contains(search)){
                    searchOccurences.set(playerId, searchOccurences.get(playerId)+1);
                }
            }
        }

        int maxOccurences = searchOccurences.stream().max(Integer::compare).get();
        int minOccurences = searchOccurences.stream().min(Integer::compare).get();
        assertTrue(maxOccurences-minOccurences <= 1,
                "All players should have played the same number of turns.");
    }

    /**
     * <H1>Requirement 13</H1>
     * Test that scores are calculated correctly
     *  This is just an example, making tests for all criteria will take a long time.
     *  Similar tests can be made for the other criteria.
     */
    @Test
    public void testScoreCalculation(){
        IAbstractPlayerAssetsFactory playerAssetsFactory1 = new PointSaladPlayerAssetsFactory(random);
        IPlayerManager playerManager1 = playerAssetsFactory1.createPlayerManager(
                2, 0);

        IPlayer player0 = playerManager1.getPlayerById(0);
        IPlayer player1 = playerManager1.getPlayerById(1);

        ICard card0 = new PointSaladCard(
                new PointsPerCopyOfOneOrManyResources(
                        new ArrayList<>(Arrays.asList(4, 3)),
                        new ArrayList<>(Arrays.asList(PointSaladResource.CABBAGE, PointSaladResource.LETTUCE))),
                PointSaladResource.LETTUCE);

        ICard card1 = new PointSaladCard(
                new PointsIfMostOrFewestTotalResources(10, true),
                PointSaladResource.CABBAGE);

        player0.addToHand(card0);
        player1.addToHand(card1);

        // Grow some cabbages
        for (int i=0; i<10; i++){
            ICard card = new PointSaladCard(
                    new PointsIfMostOrFewestTotalResources(10, true),
                    PointSaladResource.CABBAGE);
            card.flip();
            player0.addToHand(card);
        }

        // Grow some tomatoes
        for (int i=0; i<15; i++){
            ICard card = new PointSaladCard(
                    new PointsIfMostOrFewestTotalResources(10, true),
                    PointSaladResource.TOMATO);
            card.flip();
            player1.addToHand(card);
        }

        Map<IPlayer, Integer> scores = playerManager1.calculateScores();
        assertEquals(scores.get(player0), 40,
                "Player 0 should have 40 points.");
        assertEquals(scores.get(player1), 10,
                "Player 1 should have 10 points.");

    }

    /**
     * <H1>Requirement 14</H1>
     * Test that the declared winner is correct
     */
    @Test
    public void testWinner() {
        IAbstractPlayerAssetsFactory playerAssetsFactory1 = new PointSaladPlayerAssetsFactory(random);
        IPlayerManager playerManager = playerAssetsFactory1.createPlayerManager(
                6, 0);
        MockIO io = runFakeMatch(playerManager);
        Map<IPlayer, Integer> scores = playerManager.calculateScores();

        int winnerId = -1;
        for (int playerId=0; playerId<6; playerId++){
            ArrayList<String> msgs = io.getMessages(playerId);
            String search = "Congratulations! You are the winner!";
            for (String msg : msgs) {
                if (msg.contains(search)) {
                    if (winnerId != -1) {
                        fail("There should only be one winner.");
                    }
                    winnerId = playerId;
                    break;
                }
            }
        }

        int bestScore = 0;
        ArrayList<Integer> playersWithBestScore = new ArrayList<>(); // Handle case where multiple players have the same score
        for (IPlayer player : scores.keySet()){
            if (scores.get(player) > bestScore){
                bestScore = scores.get(player);
                playersWithBestScore.clear();
            }
            if (scores.get(player) == bestScore){
                playersWithBestScore.add(player.getId());
            }
        }
        if (playersWithBestScore.size() > 1){
            assertTrue(playersWithBestScore.contains(winnerId),
                    "The winner should be one of the players with the highest score.");
        } else if (playersWithBestScore.size() == 1){
            assertEquals(playersWithBestScore.get(0), winnerId,
                    "The winner should be the player with the highest score.");
        } else {
            fail("There should be at least one winner.");
        }
    }


    private MockIO runFakeMatch(IPlayerManager playerManager){
        // Reduce the piles to 6 cards each
        for (IPile pile : piles){
            while (pile.size() > 6){
                pile.drawTop();
            }
        }

        Map<Integer, ArrayList<String>> playerToActions = new HashMap<>();

        for (int playerId=0; playerId < 6; playerId++) {
            playerToActions.put(playerId, new ArrayList<>());
            for (int i = 0; i < 3; i++) {
                int pileId = playerId%3;
                playerToActions.get(playerId).add("P" + pileId);
                playerToActions.get(playerId).add("");
            }
            int marketRow = playerId%2;
            int marketCol = playerId%3;
            playerToActions.get(playerId).add("M" + marketRow + marketCol);
            playerToActions.get(playerId).add("");
        }

        MockIO io = new MockIO(playerToActions);
        ArrayList<ITurnActionStrategy> humanActions = new ArrayList<>();
        humanActions.add(new PointSaladHumanMain(gameBoard, io));
        humanActions.add(new PointSaladHumanFree(gameBoard, io));

        GameLoopTemplate gameLoop = new PointSaladGameLoop(
                io,
                playerManager,
                gameBoard,
                humanActions,
                new ArrayList<ITurnActionStrategy>()
        );
        gameLoop.startGame();

        return io;
    }
}
