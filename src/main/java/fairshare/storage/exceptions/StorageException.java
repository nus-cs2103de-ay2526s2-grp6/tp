package fairshare.storage.exceptions;

/**
 * Represents an error that occurs during a storage operation.
 */
public class StorageException extends Exception {

    /**
     * Constructs a {@code StorageException} with the given message.
     *
     * @param message the error message; cannot be null.
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code StorageException} with the given message and cause.
     *
     * @param message the error message; cannot be null.
     * @param cause the underlying cause; cannot be null.
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
