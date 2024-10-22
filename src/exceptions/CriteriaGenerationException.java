package exceptions;

/**
 * Exception thrown when a criteria cannot be generated.
 */
public class CriteriaGenerationException extends RuntimeException {
    public CriteriaGenerationException(String message) {
        super(message);
    }
}
