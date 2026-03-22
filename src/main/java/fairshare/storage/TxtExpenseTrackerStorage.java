package fairshare.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fairshare.model.expense.Expense;
import fairshare.storage.exceptions.StorageException;

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
     * @throws StorageException if the file exists but cannot be read or parsed.
     */
    @Override
    public List<Expense> readExpenseTracker() throws StorageException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            return TxtSerializableExpenseTracker
                    .loadFromFile(filePath)
                    .toModelType();
        } catch (IOException e) {
            throw new StorageException(
                    "Failed to read data from file: " + filePath, e);
        }
    }

    /**
     * Saves the given list of expenses to the local text file.
     *
     * @param expenses the list of {@code Expense} to save;
     *                 cannot be null.
     * @throws StorageException if the file cannot be written to.
     */
    @Override
    public void saveExpenseTracker(List<Expense> expenses) throws StorageException {
        try {
            new TxtSerializableExpenseTracker(expenses).saveToFile(filePath);
        } catch (IOException e) {
            throw new StorageException(
                    "Failed to save data to file: " + filePath, e);
        }
    }
}
