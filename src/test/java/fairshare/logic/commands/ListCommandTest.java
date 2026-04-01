package fairshare.logic.commands;

import fairshare.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ListCommandTest {

    // Test list command displays correct user message.
    @Test
    public void execute_list_success() {
        Model model = Mockito.mock(Model.class);
        ListCommand listCmd = new ListCommand();

        CommandResult cmdRes = listCmd.execute(model);

        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        String successMessage = "Listing all expenses.";
        assertEquals(successMessage, cmdRes.getResponse());

        verify(model).filterExpenses(any()); // Verify filterExpenses is called.
    }
}
