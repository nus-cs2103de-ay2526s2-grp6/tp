package fairshare.logic.commands.exceptions;

/**
 * Represents an error that occurred during command execution.
 */
public class CommandException extends Exception {
    /**
     * Creates an instance of {@code CommandException} with the specified error message.
     *
     * @param message The detail message specifying the cause of the error.
     */
    public CommandException(String message) {
        super(message);
    }
}
