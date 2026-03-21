package fairshare.ui.exceptions;

/**
 * Represents an error that occurs during a UI operation,
 * such as failing to load an FXML file.
 */
public class UiException extends RuntimeException {

    /**
     * Constructs a {@code UiException} with the given message.
     *
     * @param message the error message; cannot be null.
     */
    public UiException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code UiException} with the given message and cause.
     *
     * @param message the error message; cannot be null.
     * @param cause   the underlying cause; cannot be null.
     */
    public UiException(String message, Throwable cause) {
        super(message, cause);
    }
}
