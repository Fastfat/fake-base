package exceptions;

public class WrongFormatException extends NumberFormatException {
    public WrongFormatException(final String message) {
        super(message);
    }
}
