// AI declaration --> claude ai was used to generate ideas for this class; mainly how to handle file corruption

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
public class TxtFairShareStorage implements FairShareStorage {
    private final Path filePath;

    /**
     * Constructs a {@code TxtFairShareStorage} with the given file path.
     *
     * @param filePath the path of the data file to read from and write to
     */
    public TxtFairShareStorage(Path filePath) {
        assert filePath != null : "filePath should not be null";

        this.filePath = filePath;
    }

    /**
     * Returns the file path of the expense tracker data file.
     *
     * @return the {@code Path} to the data file.
     */
    @Override
    public Path getFairShareFilePath() {
        return filePath;
    }

    /**
     * Reads expense data from the local text file.
     * Returns an empty list if the file does not exist.
     * If the file is corrupted, deletes it and throws a
     * {@code StorageException} so the app can start fresh.
     *
     * @return a list of {@code Expense} with the loaded data.
     * @throws StorageException if the file is corrupted or cannot
     *                          be read.
     */
    @Override
    public List<Expense> readFairShare() throws StorageException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            return TxtSerializableFairShare
                    .loadFromFile(filePath)
                    .toModelType();
        } catch (Exception e) {
            try {
                Files.delete(filePath);
            } catch (IOException deleteError) {
                System.out.println(
                        "Could not delete corrupted file: "
                                + deleteError.getMessage());
            }
            throw new StorageException(
                    "Data file was corrupted and has been cleared. "
                            + "Starting with empty expense list.");
        }
    }

    /**
     * Saves the given list of expenses to the local text file.
     *
     * @param expenses the list of {@code Expense} to save
     * @throws StorageException if the file cannot be written to.
     */
    @Override
    public void saveFairShare(List<Expense> expenses) throws StorageException {
        assert expenses != null : "expenses should not be null";

        try {
            new TxtSerializableFairShare(expenses).saveToFile(filePath);
        } catch (IOException e) {
            throw new StorageException(
                    "Failed to save data to file: " + filePath, e);
        }
    }
}
