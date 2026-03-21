package fairshare.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fairshare.model.expense.Expense;

/**
 * An implementation of {@code ExpenseTrackerStorage} that reads and writes
 * expense data from and to a plain-text file on local disk.
 */
public class TxtExpenseTrackerStorage implements ExpenseTrackerStorage {
    private final Path filePath;

    /**
     * Constructs a {@code TxtExpenseTrackerStorage} with the given file path.
     *
     * @param filePath the path of the data file to read from and write to;
     *                 cannot be null.
     */
    public TxtExpenseTrackerStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path of the expense tracker data file.
     *
     * @return the {@code Path} to the data file.
     */
    @Override
    public Path getExpenseTrackerFilePath() {
        return filePath;
    }

    /**
     * Reads expense tracker data from the local text file.
     * Returns an empty list if the file does not exist.
     *
     * @return a list of {@code TxtAdaptedExpense} with the loaded data.
     * @throws Exception if the file exists but cannot be read or parsed.
     */
    @Override
    public List<Expense> readExpenseTracker() throws Exception {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        return TxtSerializableExpenseTracker.loadFromFile(filePath).toModelType();
    }

    /**
     * Saves the given list of expenses to the local text file.
     *
     * @param expenses the list of {@code Expense} to save;
     *                 cannot be null.
     * @throws Exception if the file cannot be written to.
     */
    @Override
    public void saveExpenseTracker(List<Expense> expenses) throws Exception {
        new TxtSerializableExpenseTracker(expenses).saveToFile(filePath);
    }
}
