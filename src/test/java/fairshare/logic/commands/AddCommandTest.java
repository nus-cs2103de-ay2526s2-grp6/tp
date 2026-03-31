package fairshare.logic.commands;

import fairshare.model.Model;
import fairshare.model.expense.Expense;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddCommandTest {

    // Test creating an instance of AddCommand with a null expense.
    @Test
    public void constructor_nullExpense_throwsAssertionError() {
        Expense expense = null;
        assertThrows(AssertionError.class, () -> new AddCommand(expense));
    }

    // Test adding a valid expense.
    @Test
    public void execute_validExpense_success() {
        Model model = Mockito.mock(Model.class);
        Expense expense = Mockito.mock(Expense.class);

        when(expense.getExpenseName()).thenReturn("lunch");

        AddCommand addCmd = new AddCommand(expense);
        CommandResult cmdRes = addCmd.execute(model);

        String success = "New expense added: lunch";
        assertEquals(success, cmdRes.getResponse());

        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        verify(model).addExpense(expense); // Verify addExpense was called.
    }

    // Test adding a null expense.
    @Test
    public void execute_nullExpense_throwsAssertionError() {
        Model model = null;
        Expense expense = Mockito.mock(Expense.class);

        AddCommand addCmd = new AddCommand(expense);
        assertThrows(AssertionError.class, () -> addCmd.execute(model));
    }
}
