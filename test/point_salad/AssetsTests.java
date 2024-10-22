package point_salad;

import assets.*;
import assets.impl.PointSaladAssetsFactory;
import assets.impl.PointSaladResource;
import common.point_salad.Constants;
import common.point_salad.ManifestMetadata;
import exceptions.CardFlippingException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AssetsTests {
    private static final int MIN_PLAYERS = Constants.MIN_PLAYERS.getValue();
    private static final int MAX_PLAYERS = Constants.MAX_PLAYERS.getValue();
    private static final int NUM_TYPES = Constants.NUM_TYPES.getValue();
    private static final int NUM_CARDS = Constants.DECK_SIZE.getValue();
    private static final int NUM_COPIES_OF_EACH_TYPE = NUM_CARDS / NUM_TYPES;
    private static final int NUM_PILES = Constants.NUM_PILES.getValue();
    private static final int CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG =
            Constants.CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG.getValue();
    private static final int CARDS_DRAWN_PER_PILE_TO_INIT_MARKET =
            Constants.CARDS_DRAWN_PER_PILE_TO_INIT_MARKET.getValue();
    private static final int MARKET_ROWS = Constants.MARKET_ROWS.getValue();
    private static final int MARKET_COLS = Constants.MARKET_COLS.getValue();
    private static final String JSON_FILENAME = ManifestMetadata.MANIFEST_FILENAME.getValue();

    private static JSONObject deckJson;
    private static IAbstractAssetsFactory factory;
    private static Random random;

    @BeforeAll
    public static void setUpAll() throws FileNotFoundException {
        random = new Random();
        deckJson = Util.fileToJSON(JSON_FILENAME);
        factory = new PointSaladAssetsFactory(random);
    }

    @AfterAll
    public static void tearDownAll() {
        deckJson = null;
        factory = null;
    }

    /**
     * <H1>Requirements 2 and 3</H1>
     * Test that the amount of cards in the deck is correct for each player count
     */
    @Test
    public void testDeckSize() {
        for (int i = MIN_PLAYERS; i <= MAX_PLAYERS; i++) {
            ArrayList<ICard> deck = factory.createDeck(deckJson, i);
            assertEquals(
                    NUM_CARDS - ((MAX_PLAYERS - i) * CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG * NUM_TYPES),
                    deck.size(),
                    "Deck size is not correct for " + i + " players");
        }
    }

    /**
     * <H1>Requirement 2</H1>
     * Test that the correct amount of each resource is present in the deck
     */
    @Test
    public void testVegetableCount(){
        for (int i = MIN_PLAYERS; i <= MAX_PLAYERS; i++) {
            ArrayList<ICard> deck = factory.createDeck(deckJson, i);
            for (IResource resource: PointSaladResource.values()) {
                int count = 0;
                for (ICard card : deck) {
                    if (card.getResource() == resource) {
                        count++;
                    }
                }
                assertEquals(
                        NUM_COPIES_OF_EACH_TYPE -
                                ((MAX_PLAYERS - i) * CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG),
                        count,
                        "Incorrect number of " + resource.represent() + " in deck for " + i + " players");
            }
        }
    }

    /**
     * <H1>Requirement 4</H1>
     * Test that the deck is being shuffled
     */
    @Test
    public void testShuffle(){
        ArrayList<ICard> deck1 = factory.createDeck(deckJson, MAX_PLAYERS);
        ArrayList<ICard> deck2 = factory.createDeck(deckJson, MAX_PLAYERS);
        assertNotEquals(deck1, deck2, "Deck was not shuffled");
    }

    /**
     * <H1>Requirement 4</H1>
     * Test that the deck is divided into 3 equal (with exception for 1 pile) piles
     */
    @Test
    public void testPileSizes(){
        for (int i = MIN_PLAYERS; i <= MAX_PLAYERS; i++) {
            ArrayList<ICard> deck = factory.createDeck(deckJson, i);
            ArrayList<IPile> piles = factory.createPiles(deck);
            int expectedSize = deck.size() / NUM_PILES;
            int remainder = deck.size() % NUM_PILES;
            boolean seenRemainder = false;

            for (IPile pile : piles) {
                int size = pile.getCardCount();
                if (size != expectedSize){
                    if (seenRemainder){
                        fail("More than one pile is not the expected size");
                    }
                    if (size != expectedSize+remainder){
                        fail("The remainder pile has the wrong size");
                    }
                    seenRemainder = true;
                }
            }
            assertTrue(true);
        }
    }
    /**
     * <H1>Requirement 5</H1>
     * Test that the market is created full with vegetables
     */
    @Test
    public void testMarketCreatedFull(){
        for (int i = MIN_PLAYERS; i <= MAX_PLAYERS; i++) {
            ArrayList<ICard> deck = factory.createDeck(deckJson, i);
            ArrayList<IPile> piles = factory.createPiles(deck);
            IMarket market = factory.createMarket(piles);
            for (int row = 0; row < MARKET_ROWS; row++) {
                for (int col = 0; col < MARKET_COLS; col++) {
                    ICard card = market.draftCard(row, col);
                    assertNotNull(card, "Market is not full");
                    assertFalse(card.isCriteriaSideActive(), "Vegetable side is not active");
                }
            }
        }
        assertTrue(true);
    }

    /**
     * <H1>Requirement 5</H1>
     * Test that each pile gave exactly 2 cards to the market when it was created
     */
    @Test
    public void testMarketPileSize(){
        for (int i = MIN_PLAYERS; i <= MAX_PLAYERS; i++) {
            ArrayList<ICard> deck = factory.createDeck(deckJson, i);
            ArrayList<IPile> piles = factory.createPiles(deck);
            factory.createMarket(piles);

            int expectedSize = (deck.size() / NUM_PILES) - CARDS_DRAWN_PER_PILE_TO_INIT_MARKET;
            int remainder = deck.size() % NUM_PILES;
            int expectedRemainderSize = expectedSize + remainder;

            boolean seenRemainder = false;
            for (IPile pile : piles) {
                int size = pile.getCardCount();
                if (size != expectedSize) {
                    if (seenRemainder) {
                        fail("More than one pile is not the expected size");
                    }
                    if (size != expectedRemainderSize) {
                        fail("The remainder pile has the wrong size");
                    }
                    seenRemainder = true;
                }
            }
        }
        assertTrue(true);
    }

    /**
     * <H1>Requirement 8</H1>
     * Test that cards can be flipped to vegetable side, but not to criteria side.
     */
    @Test
    public void testCardFlip() {
        ArrayList<ICard> deck = factory.createDeck(deckJson, MAX_PLAYERS);
        ICard card = deck.getFirst();
        assertTrue(card.isCriteriaSideActive(), "Card started with criteria side down!");
        card.flip();
        assertFalse(card.isCriteriaSideActive(), "Card did not flip to vegetable side!");
        try {
            card.flip();
            fail("Card flipped to criteria side!");
        } catch (CardFlippingException e) {
            assertTrue(true);
        }
    }
}
