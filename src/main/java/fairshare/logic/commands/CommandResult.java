package fairshare.logic.commands;

public class CommandResult {
    private final String response;

    public CommandResult(String response) {
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }
}
