package common.point_salad;

public enum Constants {
    MIN_PLAYERS(2),
    MAX_PLAYERS(6),
    DECK_SIZE(108),
    NUM_TYPES(6),
    CARDS_REMOVED_PER_MISSING_PLAYER_PER_VEG(3),
    NUM_PILES(3),
    CARDS_DRAWN_PER_PILE_TO_INIT_MARKET(2),
    MARKET_ROWS(3),
    MARKET_COLS(2);

    private final int value;

    Constants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
