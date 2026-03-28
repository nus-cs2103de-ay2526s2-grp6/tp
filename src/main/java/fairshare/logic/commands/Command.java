package fairshare.logic.commands;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;

/**
 * A general abstract class that represents a command that can be executed.
 * Serves as the foundation for all specific commands.
 */
public abstract class Command {
    public abstract CommandResult execute(Model model) throws CommandException;
}
