package exceptions;

public class ManagerDeleteException extends RuntimeException {
    public ManagerDeleteException(final String message) {
        super(message);
    }
}
