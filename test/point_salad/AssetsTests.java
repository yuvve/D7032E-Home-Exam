package point_salad;

import assets.*;
import assets.impl.PointSaladAssetsFactory;
import assets.impl.PointSaladGameBoard;
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

    /**
     * <H1>Requirement 10</H1>
     * Test that the market is correctly refilled
     */
    @Test
    public void testMarketRefill(){
        ArrayList<ICard> deck = factory.createDeck(deckJson, MAX_PLAYERS);
        ArrayList<IPile> piles = factory.createPiles(deck);
        IMarket market = factory.createMarket(piles);
        IGameBoard gameBoard = new PointSaladGameBoard(piles, market);

        int[] pilesStartingSizes = new int[NUM_PILES];
        for (int i = 0; i < NUM_PILES; i++) {
            pilesStartingSizes[i] = piles.get(i).getCardCount();
        }
        for (int row = 0; row < MARKET_ROWS; row++) {
            for (int col = 0; col < MARKET_COLS; col++) {
                market.draftCard(row, col);
                assertNull(market.viewCard(row, col), "Card was not removed from market");
                gameBoard.refillMarket();
                assertNotNull(market.viewCard(row, col), "Card was not added to market");
                assertEquals(
                        -row,
                        piles.get(col).getCardCount() - pilesStartingSizes[col],
                        "Pile " + col + " was not used to refill the market at the correct slot");
            }
        }

    }

    /**
     * <H1>Requirement 11</H1>
     * Test that piles are correctly refilled
     * Test that the bottom half of the largest pile is used to refill the smallest pile
     */
    @Test
    public void testPileRefill(){
        ArrayList<ICard> deck = factory.createDeck(deckJson, MAX_PLAYERS);
        ArrayList<IPile> piles = factory.createPiles(deck);
        IMarket market = factory.createMarket(piles);
        IGameBoard gameBoard = new PointSaladGameBoard(piles, market);

        IPile pile0 = piles.getFirst();
        IPile pile1 = piles.get(1);
        IPile pile2 = piles.get(2);

        ArrayList<ICard> discardedCards = new ArrayList<>();

        while (pile0.getCardCount() > 1){
            discardedCards.add(pile0.drawTop());
        }
        while (pile2.getCardCount() > 1){
            discardedCards.add(pile2.drawTop());
        }
        assertEquals(1, pile0.getCardCount(), "All cards were not drawn from pile 1!");
        assertEquals(1, pile2.getCardCount(), "All cards were not drawn from pile 2!");

        if (!(pile1.getCardCount() > pile0.getCardCount()) || !(pile1.getCardCount() > pile2.getCardCount())){
            fail("Pile 1 should be the largest");
        }

        ICard pile1TopCardBefore = pile1.viewTop();
        int pile1ExpectedSize;
        if (pile1.getCardCount()%2 == 0){
            pile1ExpectedSize = pile1.getCardCount()/2;
        } else {
            pile1ExpectedSize = (pile1.getCardCount()/2) + 1;
        }

        gameBoard.getCardFromPile(0);
        assertEquals(pile1.getCardCount(),
                pile1ExpectedSize,
                "Pile 1 was not correctly used to refill pile 0");
        assertEquals(pile1.viewTop(), pile1TopCardBefore,
                "The top card of pile 1 was removed during refill of pile 0!");

        // Make pile 0 the largest
        pile0.addCards(discardedCards);

        ICard pile0TopCardBefore = pile0.viewTop();
        int pile0ExpectedSize;
        if (pile0.getCardCount()%2 == 0){
            pile0ExpectedSize = pile0.getCardCount()/2;
        } else {
            pile0ExpectedSize = (pile0.getCardCount()/2) + 1;
        }

        gameBoard.getCardFromPile(2);
        assertEquals(pile0.getCardCount(),
                pile0ExpectedSize,
                "Pile 0 was not correctly used to refill pile 2");
        assertEquals(pile0.viewTop(), pile0TopCardBefore,
                "The top card of pile 0 was removed during refill of pile 2!");
    }
}
