package exceptions;

public class NetworkingException extends RuntimeException {
    public NetworkingException(String message) {
        super(message);
    }
}
