package point_salad;

import assets.ICard;
import assets.IResource;
import assets.impl.PointSaladAssetsFactory;
import assets.impl.PointSaladResource;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AssetsTests {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 6;
    private static final int NUM_TYPES = 6;
    private static final int NUM_CARDS = 108;
    private static final int NUM_PILES = 3;
    private static final int CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG = 3;
    private static final String JSON_FILENAME = "PointSaladManifest.json";

    private static JSONObject deckJson;
    private static PointSaladAssetsFactory factory;
    private static Random random;

    @BeforeAll
    public static void setUpAll() throws FileNotFoundException {
        deckJson = Utils.fileToJSON(JSON_FILENAME);
        factory = new PointSaladAssetsFactory();
        random = new Random();
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

    @Test
    public void testCards(){
        ArrayList<ICard> deck = factory.createDeck(deckJson, MAX_PLAYERS);
        // Choose one card at random
        ICard card = deck.get(random.nextInt(deck.size()));
        if (!isResourceValid(card.getResource())) {
            fail("Card" + card.represent() + " does not have a valid resource (" + card.getResource().represent() + ")");
        }
        // Test criteria side
        assertTrue(true);
    }

    private boolean isResourceValid(IResource resource) {
        for (PointSaladResource validResource : PointSaladResource.values()) {
            if (resource == validResource) {
                return true;
            }
        }
        return false;
    }
}
