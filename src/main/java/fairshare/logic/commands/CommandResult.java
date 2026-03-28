package fairshare.logic.commands;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    private final String response;
    private final boolean isHelp;
    private final boolean isExit;

    /**
     * Creates an instance of {@code CommandResult} with the specified response message and state flags.
     *
     * @param response The response message to be displayed to the user.
     * @param isHelp True if the command requires the help window to be displayed (help command)
     * @param isExit True if the command requires the program to shut down (exit command)
     */
    public CommandResult(String response, boolean isHelp, boolean isExit) {
        this.response = response;
        this.isHelp = isHelp;
        this.isExit = isExit;
    }

    /**
     * Returns the response message to be displayed.
     *
     * @return The response string.
     */
    public String getResponse() {
        return this.response;
    }

    /**
     * Returns the help status after command execution.
     *
     * @return True if the help window needs to be displayed, false otherwise.
     */
    public boolean getIsHelp() {
        return this.isHelp;
    }

    /**
     * Returns the exit status after command execution.
     *
     * @return True if the program needs to be shut down, false otherwise.
     */
    public boolean getIsExit() {
        return this.isExit;
    }
}
