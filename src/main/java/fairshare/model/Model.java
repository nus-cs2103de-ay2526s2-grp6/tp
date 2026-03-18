package fairshare.model;

import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;

import java.util.List;

public interface Model {
    void addExpense(Expense expense);
    void deleteExpense(int index);
    List<Balance> getSimplifiedBalances();
}
