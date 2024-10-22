package exceptions;

/**
 * Exception thrown when the game fails to send or receive network data.
 */
public class NetworkingException extends RuntimeException {
    public NetworkingException(String message) {
        super(message);
    }
}
