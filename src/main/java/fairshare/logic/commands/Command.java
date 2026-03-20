package fairshare.logic.commands;

import fairshare.logic.commands.exceptions.CommandException;
import fairshare.model.Model;

public abstract class Command {
    public abstract CommandResult execute(Model model) throws CommandException;
}
