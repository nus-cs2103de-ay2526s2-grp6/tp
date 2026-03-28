package fairshare.logic.parser.exceptions;

/**
 * Represents an error that occurred during command parsing.
 */
public class ParseException extends Exception {

    /**
     * Creates an instance of {@code ParseException} with the specified error message.
     *
     * @param message The detail message specifying the cause of the error.
     */
    public ParseException(String message) {
        super(message);
    }
}
