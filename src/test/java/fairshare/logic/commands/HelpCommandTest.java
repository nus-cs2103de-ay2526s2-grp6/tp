package fairshare.logic.commands;

import fairshare.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class HelpCommandTest {

    // Test help command displays correct user message and sets the appropriate flag.
    @Test
    public void execute_help_success() {
        Model model = Mockito.mock(Model.class);
        HelpCommand helpCmd = new HelpCommand();

        CommandResult cmdRes = helpCmd.execute(model);

        assertTrue(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        String successMessage = "Displaying help window";
        assertEquals(successMessage, cmdRes.getResponse());
    }
}
