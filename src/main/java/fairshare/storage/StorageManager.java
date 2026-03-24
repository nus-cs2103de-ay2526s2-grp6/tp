package fairshare.storage;

import java.nio.file.Path;
import java.util.List;

import fairshare.model.expense.Expense;
import fairshare.storage.exceptions.StorageException;

/**
 * Manages all storage operations for the Shared Expense Tracker.
 * Acts as the single entry point for storage interactions,
 * delegating to the underlying {@code ExpenseTrackerStorage} implementation.
 */
public class StorageManager implements Storage {

    private final FairShareStorage fairShareStorage;

    /**
     * Constructs a {@code StorageManager} with the given storage implementation.
     *
     * @param fairShareStorage the storage implementation to delegate to;
     *                              cannot be null.
     */
    public StorageManager(FairShareStorage fairShareStorage) {
        this.fairShareStorage = fairShareStorage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getFairShareFilePath() {
        return fairShareStorage.getFairShareFilePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Expense> readFairShare() throws StorageException {
        return fairShareStorage.readFairShare();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFairShare(List<Expense> expenses) throws StorageException {
        fairShareStorage.saveFairShare(expenses);
    }
}
