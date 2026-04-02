package fairshare.logic.commands;

import fairshare.model.Model;
import org.mockito.Mockito;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExitCommandTest {

    // Test exit command displays correct user message and sets the appropriate flag.
    @Test
    public void execute_exit_success() {
        Model model = Mockito.mock(Model.class);
        ExitCommand exitCmd = new ExitCommand();

        CommandResult cmdRes = exitCmd.execute(model);

        assertFalse(cmdRes.getIsHelp());
        assertTrue(cmdRes.getIsExit());

        String successMessage = "Shutting down FairShare...\nSee you soon!";
        assertEquals(successMessage, cmdRes.getResponse());
    }

}
