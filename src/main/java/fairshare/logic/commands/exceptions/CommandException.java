package fairshare.logic.commands.exceptions;

import fairshare.logic.exceptions.FairShareException;

public class CommandException extends FairShareException {
    public CommandException(String message) {
        super(message);
    }
}
