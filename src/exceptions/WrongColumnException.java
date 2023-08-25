package exceptions;

public class WrongColumnException extends IllegalArgumentException {
    public WrongColumnException(final String message) {
        super(message);
    }
}
