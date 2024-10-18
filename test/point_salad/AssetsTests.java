package point_salad;

import assets.ICard;
import assets.IMarket;
import assets.IPile;
import assets.IResource;
import assets.impl.PointSaladAssetsFactory;
import assets.impl.PointSaladResource;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Util;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AssetsTests {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 6;
    private static final int NUM_TYPES = 6;
    private static final int NUM_CARDS = 108;
    private static final int NUM_COPIES_OF_EACH_TYPE = NUM_CARDS / NUM_TYPES;
    private static final int NUM_PILES = 3;
    private static final int CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG = 3;
    private static final int CARDS_DRAWN_PER_PILE_TO_INIT_MARKET = 2;
    private static final String JSON_FILENAME = "PointSaladManifest.json";

    private static JSONObject deckJson;
    private static PointSaladAssetsFactory factory;

    @BeforeAll
    public static void setUpAll() throws FileNotFoundException {
        deckJson = Util.fileToJSON(JSON_FILENAME);
        factory = new PointSaladAssetsFactory();
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
            for (ICard[] row : market.viewCards()) {
                for (ICard card : row) {
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
}
