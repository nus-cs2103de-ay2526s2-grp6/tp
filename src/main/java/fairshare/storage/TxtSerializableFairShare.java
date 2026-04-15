// AI declaration --> claude ai was used to generate ideas for this class

package fairshare.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import fairshare.model.expense.Expense;

/**
 * Serialises and deserialises the full expense list to and from
 * a plain-text file, where each line represents one expense.
 */
public class TxtSerializableFairShare {

    private final List<TxtAdaptedExpense> expenses;

    /**
     * Constructs a {@code TxtSerializableExpenseTracker} from a list of
     * {@code Expense} model objects.
     *
     * @param expenses the list of expenses to serialise; cannot be null.
     */
    public TxtSerializableFairShare(List<Expense> expenses) {
        this.expenses = expenses.stream()
                .map(TxtAdaptedExpense::new)
                .toList();
    }

    /**
     * Returns the list of adapted expenses held by this tracker.
     *
     * @return a list of {@code TxtAdaptedExpense}.
     */
    public List<TxtAdaptedExpense> getExpenses() {
        return expenses;
    }

    /**
     * Converts all adapted expenses back into {@code Expense} model objects.
     *
     * @return a list of {@code Expense}.
     */
    public List<Expense> toModelType() {
        return expenses.stream()
                .map(TxtAdaptedExpense::toModelType)
                .toList();
    }

    /**
     * Writes all expenses to the specified file path, one expense per line.
     * Creates parent directories if they do not exist.
     *
     * @param filePath the path to write to; cannot be null.
     * @throws IOException if the file cannot be written to.
     */
    public void saveToFile(Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());

        List<String> lines = expenses.stream()
                .map(TxtAdaptedExpense::serialize)
                .toList();

        Files.write(filePath, lines);
    }

    /**
     * Reads all expenses from the specified file path and returns a
     * {@code TxtSerializableExpenseTracker} containing the parsed data.
     *
     * @param filePath the path to read from; cannot be null.
     * @return a {@code TxtSerializableExpenseTracker} with loaded expenses.
     * @throws IOException if the file cannot be read.
     */
    public static TxtSerializableFairShare loadFromFile(Path filePath)
            throws IOException {
        List<Expense> expenses = Files.readAllLines(filePath)
                .stream()
                .filter(line -> !line.isBlank())
                .map(TxtAdaptedExpense::deserialize)
                .map(TxtAdaptedExpense::toModelType)
                .toList();

        return new TxtSerializableFairShare(expenses);
    }
}
