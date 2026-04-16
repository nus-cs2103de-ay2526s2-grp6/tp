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
     * @param fairShareStorage the storage implementation to delegate to
     */
    public StorageManager(FairShareStorage fairShareStorage) {
        assert fairShareStorage != null : "fairShareStorage should not be null";

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
        assert expenses != null : "expenses should not be null";

        fairShareStorage.saveFairShare(expenses);
    }
}
