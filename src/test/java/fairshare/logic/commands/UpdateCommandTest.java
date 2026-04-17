package fairshare.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;
import fairshare.model.expense.Expense;
import fairshare.model.expense.ExpenseType;
import fairshare.model.expense.Participant;
import fairshare.model.group.Group;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UpdateCommandTest {

    // Test creating an instance of UpdateCommand with null UpdateFields.
    @Test
    public void constructor_nullUpdateFields_throwsAssertionError() {
        UpdateCommand.UpdateFields updateFields = null;

        assertThrows(AssertionError.class, () -> new UpdateCommand(0, updateFields));
    }

    // Test executing update command with a null model.
    @Test
    public void execute_nullModel_throwsAssertionError() {
        Model model = null;
        UpdateCommand.UpdateFields updateFields = Mockito.mock(UpdateCommand.UpdateFields.class);
        UpdateCommand updateCmd = new UpdateCommand(0, updateFields);

        assertThrows(AssertionError.class, () -> updateCmd.execute(model));
    }

    // Test executing update command with an index that is out of bound.
    @Test
    public void execute_indexOutOfBound_throwsCommandException() {
        Model model = Mockito.mock(Model.class);
        UpdateCommand.UpdateFields updateFields = Mockito.mock(UpdateCommand.UpdateFields.class);
        UpdateCommand updateCmd = new UpdateCommand(2, updateFields);

        Expense expense1 = Mockito.mock(Expense.class);
        Expense expense2 = Mockito.mock(Expense.class);
        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense1, expense2));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        CommandException e = assertThrows(CommandException.class, () -> updateCmd.execute(model));
        String errorMessage = "Cannot update an expense that is not in the list.";
        assertEquals(errorMessage, e.getMessage());
    }

    // Test updating command the participants of an expense with incorrect prefix (r/ instead of s/).
    @Test
    public void execute_expenseInvalidParticipantPrefix_throwsCommandException() {
        Model model = Mockito.mock(Model.class);
        Expense expense = Mockito.mock(Expense.class);
        when(expense.getExpenseType()).thenReturn(ExpenseType.EXPENSE);

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        UpdateCommand.UpdateFields updateFields = new UpdateCommand.UpdateFields();
        updateFields.setReceiver(Mockito.mock(Participant.class));

        UpdateCommand updateCmd = new UpdateCommand(0, updateFields);
        CommandException e = assertThrows(CommandException.class, () -> updateCmd.execute(model));

        String errorMessage = "Please use s/SHARER to update the participants of an expense.";
        assertEquals(errorMessage, e.getMessage());
    }

    // Test modifying settlement name using update command.
    @Test
    public void execute_settlementNameUnmodifiable_throwsCommandException() {
        Model model = Mockito.mock(Model.class);
        Expense expense = Mockito.mock(Expense.class);
        when(expense.getExpenseType()).thenReturn(ExpenseType.SETTLEMENT);

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        UpdateCommand.UpdateFields updateFields = new UpdateCommand.UpdateFields();
        updateFields.setExpenseName("new name");

        UpdateCommand updateCmd = new UpdateCommand(0, updateFields);
        CommandException e = assertThrows(CommandException.class, () -> updateCmd.execute(model));

        String errorMessage = "Cannot modify the name of a settlement.";
        assertEquals(errorMessage, e.getMessage());
    }

    // Test tagging a settlement using the update command.
    @Test
    public void execute_settlementDisallowTags_throwsCommandException() {
        Model model = Mockito.mock(Model.class);
        Expense expense = Mockito.mock(Expense.class);
        when(expense.getExpenseType()).thenReturn(ExpenseType.SETTLEMENT);

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        UpdateCommand.UpdateFields updateFields = new UpdateCommand.UpdateFields();
        Set<Tag> tags = Set.of(new Tag("food"));
        updateFields.setTags(tags);

        UpdateCommand updateCmd = new UpdateCommand(0, updateFields);
        CommandException e = assertThrows(CommandException.class, () -> updateCmd.execute(model));

        String errorMessage = "Settlements cannot be tagged.";
        assertEquals(errorMessage, e.getMessage());
    }

    // Test updating the receiver of a settlement using incorrect prefix (s/ instead of r/).
    @Test
    public void execute_settlementInvalidReceiverPrefix_throwsCommandException() {
        Model model = Mockito.mock(Model.class);
        Expense expense = Mockito.mock(Expense.class);
        when(expense.getExpenseType()).thenReturn(ExpenseType.SETTLEMENT);

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        UpdateCommand.UpdateFields updateFields = new UpdateCommand.UpdateFields();
        Set<Participant> participants = Set.of(Mockito.mock(Participant.class));
        updateFields.setParticipants(participants);

        UpdateCommand updateCmd = new UpdateCommand(0, updateFields);
        CommandException e = assertThrows(CommandException.class, () -> updateCmd.execute(model));

        String errorMessage = "Please use r/RECEIVER to update the receiver of a settlement.";
        assertEquals(errorMessage, e.getMessage());
    }

    // Test updating an expense with valid prefixes.
    @Test
    public void execute_expenseValidUpdate_success() throws CommandException {
        Model model = Mockito.mock(Model.class);
        Expense expense = createMockExpense(ExpenseType.EXPENSE);

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        UpdateCommand.UpdateFields updateFields = new UpdateCommand.UpdateFields();
        updateFields.setExpenseName("new"); // Update expense name.
        updateFields.setAmount(15.00d); // Update expense amount.

        UpdateCommand updateCmd = new UpdateCommand(0, updateFields);
        CommandResult cmdRes = updateCmd.execute(model);

        String successMessage = "Update success";
        assertEquals(successMessage, cmdRes.getResponse());
        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        // Verify updateExpense() was called with old expense and an updated expense.
        ArgumentCaptor<Expense> expenseCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(model).updateExpense(eq(expense), expenseCaptor.capture());

        // Verify updated expense fields.
        Expense updatedExpense = expenseCaptor.getValue();
        assertEquals("new", updatedExpense.getExpenseName());
        assertEquals(15.00d, updatedExpense.getAmount());
    }

    // Test updating a settlement with valid prefixes.
    @Test
    public void execute_settlementValidUpdate_success() throws CommandException {
        Model model = Mockito.mock(Model.class);
        Expense expense = createMockExpense(ExpenseType.SETTLEMENT);

        ObservableList<Expense> filteredExpenseList = FXCollections.observableArrayList(List.of(expense));
        when(model.getFilteredExpenseList()).thenReturn(filteredExpenseList);

        UpdateCommand.UpdateFields updateFields = new UpdateCommand.UpdateFields();
        updateFields.setReceiver(new Participant(new Person("john"), 1)); // Update receiver.
        updateFields.setAmount(20.00d); // Update expense amount.

        UpdateCommand updateCmd = new UpdateCommand(0, updateFields);
        CommandResult cmdRes = updateCmd.execute(model);

        String successMessage = "Update success";
        assertEquals(successMessage, cmdRes.getResponse());
        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        // Verify updateExpense() was called with old expense and an updated expense.
        ArgumentCaptor<Expense> expenseCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(model).updateExpense(eq(expense), expenseCaptor.capture());

        // Verify updated expense fields.
        Expense updatedExpense = expenseCaptor.getValue();
        assertTrue(updatedExpense.getParticipants().contains(new Participant(new Person("john"), 1)));
        assertEquals(20.00d, updatedExpense.getAmount());
    }

    private Expense createMockExpense(ExpenseType expenseType) {
        Expense expense = Mockito.mock(Expense.class);

        when(expense.getExpenseType()).thenReturn(expenseType);
        when(expense.getGroup()).thenReturn(Mockito.mock(Group.class));
        when(expense.getExpenseName()).thenReturn("old");
        when(expense.getAmount()).thenReturn(5.00);
        when(expense.getPayer()).thenReturn(Mockito.mock(Person.class));

        Participant participant = Mockito.mock(Participant.class);
        when(expense.getParticipants()).thenReturn(Set.of(participant));

        return expense;
    }
}
