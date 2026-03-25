package fairshare.storage;

import java.nio.file.Path;
import java.util.List;

import fairshare.model.expense.Expense;
import fairshare.storage.exceptions.StorageException;

/**
 * Represents a storage handler for FairShare data.
 * Defines the contract for reading and writing expense tracker data
 * to and from a persistent data source.
 */
public interface FairShareStorage {

    /**
     * Returns the file path of the expense tracker data file.
     *
     * @return the {@code Path} to the data file.
     */
    Path getFairShareFilePath();

    /**
     * Reads expense tracker data from the storage file.
     * Returns an empty list if the file does not exist.
     *
     * @return a list of {@code Expense} populated with stored data.
     * @throws StorageException if the file cannot be read or is corrupted.
     */
    List<Expense> readFairShare() throws StorageException;

    /**
     * Saves the given list of expenses to the storage file.
     *
     * @param expenses the list of {@code Expense} to save; cannot be null.
     * @throws StorageException if the file cannot be written to.
     */
    void saveFairShare(List<Expense> expenses) throws StorageException;
}
