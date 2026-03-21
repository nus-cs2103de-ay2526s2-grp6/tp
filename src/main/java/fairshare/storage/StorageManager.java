package fairshare.storage;

import java.nio.file.Path;
import java.util.List;

import fairshare.model.expense.Expense;

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
     * Returns the file path of the expense tracker data file.
     *
     * @return the {@code Path} to the data file.
     */
    @Override
    public Path getExpenseTrackerFilePath() {
        return expenseTrackerStorage.getExpenseTrackerFilePath();
    }

    /**
     * Reads expense tracker data from storage.
     *
     * @return a list of {@code Expense} with the loaded data.
     * @throws Exception if the file cannot be read or is corrupted.
     */
    @Override
    public List<Expense> readExpenseTracker() throws Exception {
        return expenseTrackerStorage.readExpenseTracker();
    }

    /**
     * Saves the given list of expenses to storage.
     *
     * @param expenses the list of {@code TxtAdaptedExpense} to save;
     *                 cannot be null.
     * @throws Exception if the file cannot be written to.
     */
    @Override
    public void saveExpenseTracker(List<Expense> expenses) throws Exception {
        expenseTrackerStorage.saveExpenseTracker(expenses);
    }
}
