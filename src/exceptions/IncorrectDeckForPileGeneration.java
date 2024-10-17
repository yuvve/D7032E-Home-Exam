package exceptions;

public class IncorrectDeckForPileGeneration extends RuntimeException {
    public IncorrectDeckForPileGeneration(String message) {
        super(message);
    }
}
