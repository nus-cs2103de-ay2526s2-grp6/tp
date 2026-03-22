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

    private final ExpenseTrackerStorage expenseTrackerStorage;

    /**
     * Constructs a {@code StorageManager} with the given storage implementation.
     *
     * @param expenseTrackerStorage the storage implementation to delegate to;
     *                              cannot be null.
     */
    public StorageManager(ExpenseTrackerStorage expenseTrackerStorage) {
        this.expenseTrackerStorage = expenseTrackerStorage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getExpenseTrackerFilePath() {
        return expenseTrackerStorage.getExpenseTrackerFilePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Expense> readExpenseTracker() throws StorageException {
        return expenseTrackerStorage.readExpenseTracker();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveExpenseTracker(List<Expense> expenses)
            throws StorageException {
        expenseTrackerStorage.saveExpenseTracker(expenses);
    }
}
