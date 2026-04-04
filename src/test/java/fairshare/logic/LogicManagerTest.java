package fairshare.logic;

import fairshare.logic.commands.CommandResult;
import fairshare.logic.commands.exceptions.CommandException;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.Model;
import fairshare.model.balance.Balance;
import fairshare.model.expense.Expense;
import fairshare.storage.Storage;

import fairshare.storage.exceptions.StorageException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicManagerTest {
    private Model model;
    private Storage storage;

    // Test creating an instance of LogicManager with a null model.
    @Test
    public void constructor_nullModel_throwsAssertionError() {
        model = null;
        storage = Mockito.mock(Storage.class);

        assertThrows(AssertionError.class, () -> new LogicManager(model, storage));
    }

    // Test creating an instance of LogicManager with a null storage.
    @Test
    public void constructor_nullStorage_throwsAssertionError() {
        model = Mockito.mock(Model.class);
        storage = null;

        assertThrows(AssertionError.class, () -> new LogicManager(null, storage));
    }

    // Test executing a valid command.
    @Test
    public void execute_validCommand_success() throws ParseException, CommandException, StorageException {
        model = Mockito.mock(Model.class);
        storage = Mockito.mock(Storage.class);
        LogicManager logicManager = new LogicManager(model, storage);

        String userInput = "list";

        CommandResult cmdRes =  logicManager.execute(userInput);

        String listSuccessMessage = "Listing all expenses.";
        assertEquals(listSuccessMessage, cmdRes.getResponse());

        verify(storage).saveFairShare(any()); // verify saveFairShare() is called.
    }

    // Test executing an invalid command.
    @Test
    public void execute_invalidCommand_throwsParseException() throws ParseException, CommandException {
        model = Mockito.mock(Model.class);
        storage = Mockito.mock(Storage.class);
        LogicManager logicManager = new LogicManager(model, storage);

        String userInput = "hi";

        ParseException e = assertThrows(ParseException.class, () -> logicManager.execute(userInput));

        String errorMessage = "Invalid command: " + userInput
                + "\nType \"help\" to view a list of available commands.";
        assertEquals(errorMessage, e.getMessage());
    }

    // Test calculating balances.
    @Test
    public void calculateBalances_callsModel() {
        model = Mockito.mock(Model.class);
        storage = Mockito.mock(Storage.class);
        LogicManager logicManager = new LogicManager(model, storage);

        List<Balance> balances = List.of();
        when(model.calculateBalances()).thenReturn(balances);

        assertEquals(balances, logicManager.calculateBalances());
        verify(model).calculateBalances(); // Verify model.calculateBalances() is called.
    }

    // Test getting filtered expense list from model.
    @Test
    public void getFilteredExpenseList_callsModel() {
        model = Mockito.mock(Model.class);
        storage = Mockito.mock(Storage.class);
        LogicManager logicManager = new LogicManager(model, storage);

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList();
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        assertEquals(filteredExpenseList, logicManager.getFilteredExpenseList());
        verify(model).getFilteredExpenseList(); // Verify model.getFilteredExpenseList() is called.
    }

    // Test getting original expense list from model.
    @Test
    public void getExpenseList_callsModel() {
        model = Mockito.mock(Model.class);
        storage = Mockito.mock(Storage.class);
        LogicManager logicManager = new LogicManager(model, storage);

        List<Expense> expenseList = List.of();
        when(model.getExpenseList()).thenReturn(expenseList);

        assertEquals(expenseList, logicManager.getExpenseList());
        verify(model).getExpenseList(); // Verify model.getExpenseList() is called.
    }
}
