package fairshare.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fairshare.model.Model;
import fairshare.model.expense.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilterCommandTest {
    private Predicate<Expense> viewAll = x -> true;

    // Test creating an instance of FilterCommand with a null predicate.
    @Test
    public void constructor_nullPredicate_throwsAssertionError() {
        Predicate<Expense> predicate = null;

        assertThrows(AssertionError.class, () -> new FilterCommand(predicate));
    }

    // Test executing filter command with a null model.
    @Test
    public void execute_nullModel_throwsAssertionError() {
        Model model = null;
        FilterCommand filterCmd = new FilterCommand(viewAll);

        assertThrows(AssertionError.class, () -> filterCmd.execute(model));
    }

    // Test filtering the expense list with a valid model and predicate.
    @Test
    public void execute_validModelAndPredicate_success() {
        Model model = Mockito.mock(Model.class);

        ObservableList<Expense> expenses = FXCollections.observableArrayList(List.of(Mockito.mock(Expense.class)));
        when(model.getFilteredExpenseList()).thenReturn(expenses);

        FilterCommand filterCmd = new FilterCommand(viewAll);
        CommandResult cmdRes = filterCmd.execute(model);

        String successMessage = "Filter success. Displaying 1 expenses.";
        assertEquals(successMessage, cmdRes.getResponse());

        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        verify(model).filterExpenses(viewAll); // Check if filterExpenses was called with correct predicate.
    }
}
