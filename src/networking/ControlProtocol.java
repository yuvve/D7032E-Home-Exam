package networking;

/**
 * Enum for control protocols to assist in communication between server and client.
 */
public enum ControlProtocol {
    TRANSMISSION_OVER("TRANSMISSION_OVER"),
    GAME_OVER("GAME_OVER");

    private final String value;

    ControlProtocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
