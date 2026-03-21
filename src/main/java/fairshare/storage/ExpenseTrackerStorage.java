package fairshare.storage;

import java.nio.file.Path;
import java.util.List;
import fairshare.model.expense.Expense;

/**
 * Represents a storage handler for the ExpenseTracker data.
 * Defines the contract for reading and writing expense tracker data.
 */
public interface ExpenseTrackerStorage {

    /**
     * Returns the file path of the expense tracker data file.
     *
     * @return the {@code Path} to the data file.
     */
    Path getExpenseTrackerFilePath();

    /**
     * Reads expense tracker data from the storage file.
     * Returns an empty list if the file does not exist
     *
     * @return a list of {@code Expense} populated with stored data.
     * @throws Exception if the file cannot be read or is corrupted.
     */
    List<Expense> readExpenseTracker() throws Exception;

    /**
     * Saves the given list of expenses to the storage file.
     *
     * @param expenses the list of {@code TxtAdaptedExpense} to save;
     *                 cannot be null.
     * @throws Exception if the file cannot be written to.
     */
    void saveExpenseTracker(List<Expense> expenses) throws Exception;
}
