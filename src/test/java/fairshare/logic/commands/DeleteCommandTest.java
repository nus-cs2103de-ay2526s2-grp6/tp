package fairshare.logic.commands;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


public class DeleteCommandTest {

    // Test delete using a valid expense index.
    @Test
    public void execute_validIndex_success() throws CommandException {
        Model model = Mockito.mock(Model.class);
        Expense expense1 = Mockito.mock(Expense.class);
        Expense expense2 = Mockito.mock(Expense.class);

        when(expense1.getExpenseName()).thenReturn("lunch");
        when(expense2.getExpenseName()).thenReturn("dinner");

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense1, expense2));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        DeleteCommand deleteCmd = new DeleteCommand(1);
        CommandResult cmdRes = deleteCmd.execute(model);

        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        // Verify success message
        String success = String.format("Deleted expense: %s", expense2.getExpenseName());
        assertEquals(success, cmdRes.getResponse());

        verify(model).deleteExpense(expense2); // Verify deleteExpense was called with expense2 as arg.
        verify(model, never()).deleteExpense(expense1); // Verify expense1 was not called for deletion.
    }

    // Test delete using an out-of-bounds expense index (greater than expense list size).
    @Test
    public void execute_indexOutOfBounds_throwsCommandException() {
        Model model = Mockito.mock(Model.class);
        Expense expense1 = Mockito.mock(Expense.class);
        Expense expense2 = Mockito.mock(Expense.class);

        when(expense1.getExpenseName()).thenReturn("lunch");
        when(expense2.getExpenseName()).thenReturn("dinner");

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense1, expense2));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        DeleteCommand deleteCmd = new DeleteCommand(5);
        CommandException e = assertThrows(CommandException.class, () -> deleteCmd.execute(model));

        // Verify error message.
        String errorMessage = "Cannot delete an expense that is not in the list.";
        assertEquals(errorMessage, e.getMessage());
    }

    // Test delete using a negative expense index.
    @Test
    public void execute_indexNegative_throwsCommandException() {
        Model model = Mockito.mock(Model.class);
        Expense expense1 = Mockito.mock(Expense.class);
        Expense expense2 = Mockito.mock(Expense.class);

        when(expense1.getExpenseName()).thenReturn("lunch");
        when(expense2.getExpenseName()).thenReturn("dinner");

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense1, expense2));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        DeleteCommand deleteCmd = new DeleteCommand(-2);
        CommandException e = assertThrows(CommandException.class, () -> deleteCmd.execute(model));

        // Verify error message.
        String errorMessage = "Cannot delete an expense that is not in the list.";
        assertEquals(errorMessage, e.getMessage());
    }
}
