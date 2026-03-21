package fairshare.logic;

import java.util.List;

import javafx.collections.ObservableList;

import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;

public interface Logic {
    public CommandResult execute(String userInput) throws ParseException, CommandException;

    ObservableList<Expense> getFilteredExpenseList();

    List<Balance> calculateBalances();
}
