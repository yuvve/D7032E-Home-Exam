package networking;

/**
 * Enum for control protocols (so both client and server can use the same tokens for ending transmissions).
 */
public enum ControlProtocol {
    TRANSMISSION_OVER("TRANSMISSION_OVER");

    private final String value;

    ControlProtocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
