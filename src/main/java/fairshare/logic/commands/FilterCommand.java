package fairshare.logic.commands;

import java.util.function.Predicate;

import fairshare.model.Model;
import fairshare.model.expense.Expense;

public class FilterCommand extends Command {
    private Predicate<Expense> predicate;
    private static final String MESSAGE_SUCCESS = "Filter success.";

    public FilterCommand(Predicate<Expense> predicate) {
        this.predicate = predicate;
    }

    public CommandResult execute(Model model) {
        model.filterExpenses(predicate);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    public Predicate<Expense> getPredicate() {
        return predicate;
    }
}
