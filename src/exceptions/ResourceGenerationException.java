package exceptions;

/**
 * Exception thrown when criteria generation fails.
 */
public class ResourceGenerationException extends RuntimeException {
    public ResourceGenerationException(String message) {
        super(message);
    }
}
