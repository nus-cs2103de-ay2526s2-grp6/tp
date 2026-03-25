package fairshare.logic.commands;

public class CommandResult {
    private final String response;
    private final boolean isHelp;
    private final boolean isExit;

    public CommandResult(String response, boolean isHelp, boolean isExit) {
        this.response = response;
        this.isHelp = isHelp;
        this.isExit = isExit;
    }

    public String getResponse() {
        return this.response;
    }

    public boolean getIsHelp() {
        return this.isHelp;
    }

    public boolean getIsExit() {
        return this.isExit;
    }
}
