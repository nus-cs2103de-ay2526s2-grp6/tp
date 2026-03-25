package fairshare.model;

import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Predicate;

public interface Model {
    void addExpense(Expense expense);
    Expense deleteExpense(int index);
    void filterExpenses(Predicate<Expense> predicate);
    ObservableList<Expense> getFilteredExpenseList();
    List<Expense> getExpenseList();
    List<Balance> calculateBalances();
}
