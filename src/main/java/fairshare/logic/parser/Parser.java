package fairshare.logic.parser;

import fairshare.logic.commands.Command;
import fairshare.logic.parser.exceptions.ParseException;

public interface Parser {
    public Command parse(String args) throws ParseException;
}
