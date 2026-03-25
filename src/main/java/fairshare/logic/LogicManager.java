package fairshare.logic;

import java.util.List;

import fairshare.logic.commands.Command;
import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.FairShareParser;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.Model;
import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import fairshare.storage.Storage;
import fairshare.storage.exceptions.StorageException;
import javafx.collections.ObservableList;

/**
 * The main logic manager. Parses user input, executes commands
 * against the model, and saves state to storage after each command.
 */
public class LogicManager implements Logic {

    private final FairShareParser fairShareParser;
    private final Model model;
    private final Storage storage;

    /**
     * Constructs a {@code LogicManager} with the given model and storage.
     *
     * @param model   the model to execute commands against; cannot be null.
     * @param storage the storage to save state to; cannot be null.
     */
    public LogicManager(Model model, Storage storage) {
        this.fairShareParser = new FairShareParser();
        this.model = model;
        this.storage = storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(String userInput) throws ParseException, CommandException {
        Command cmd = fairShareParser.parseCommand(userInput);
        CommandResult result = cmd.execute(model);

        try {
            storage.saveFairShare(model.getExpenseList());
        } catch (StorageException e) {
            throw new CommandException("Could not save data: " + e.getMessage());
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return model.getFilteredExpenseList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Balance> calculateBalances() {
        return model.calculateBalances();
    }
}
