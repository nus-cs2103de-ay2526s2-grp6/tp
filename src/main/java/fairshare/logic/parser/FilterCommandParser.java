package fairshare.logic.parser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import fairshare.logic.commands.FilterCommand;
import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Expense;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser {

    /**
     * Parses the given {@code String} of arguments in the context of FilterCommand.
     *
     * @param args The raw string of user-input arguments.
     * @return A {@code FilterCommand} object.
     * @throws ParseException If the user input does not match the required format.
     */
    public FilterCommand parse(String args) throws ParseException {
        Map<String, List<String>> map = ParserUtil.tokenize(args);
        Optional<List<String>> groupNames = ParserUtil.getOptionalMultiFieldData(map, "g");
        Optional<List<String>> expenseNames = ParserUtil.getOptionalMultiFieldData(map, "n");
        Optional<List<String>> payerNames = ParserUtil.getOptionalMultiFieldData(map, "p");
        Optional<List<String>> participantNames = ParserUtil.getOptionalMultiFieldData(map, "s");
        Optional<List<String>> tagNames = ParserUtil.getOptionalMultiFieldData(map, "t");

        Predicate<Expense> predicate = expense -> true; // Matches all

        if (groupNames.isPresent()) {
            predicate = predicate.and(generatePredicateGroups(groupNames.get()));
        }
        if (expenseNames.isPresent()) {
            predicate = predicate.and(generatePredicateExpenseName(expenseNames.get()));
        }
        if (payerNames.isPresent()) {
            predicate = predicate.and(generatePredicatePayers(payerNames.get()));
        }
        if (participantNames.isPresent()) {
            predicate = predicate.and(generatePredicateParticipants(participantNames.get()));
        }
        if (tagNames.isPresent()) {
            predicate = predicate.and(generatePredicateTags(tagNames.get()));
        }

        return new FilterCommand(predicate);
    }

    private Predicate<Expense> generatePredicateGroups(List<String> groupNames) {
        return expense -> groupNames.stream()
                .anyMatch(groupName -> expense.getGroup().getGroupName().equalsIgnoreCase(groupName));
    }

    private Predicate<Expense> generatePredicateExpenseName(List<String> expenseNames) {
        return expense -> expenseNames.stream()
                .anyMatch(name -> expense.getExpenseName().equals(name));
    }

    private Predicate<Expense> generatePredicatePayers(List<String> payerNames) {
        return expense -> payerNames.stream()
                .anyMatch(payerName -> expense.getPayer().getName().equals(payerName));
    }

    private Predicate<Expense> generatePredicateParticipants(List<String> participantNames) {
        // True if ANY of the specified participants are involved in an expense
        return expense -> participantNames.stream()
                .anyMatch(participantName -> expense.getParticipants().stream()
                        .anyMatch(p -> p.getPerson().getName().equals(participantName)));
    }

    private Predicate<Expense> generatePredicateTags(List<String> tagNames) {
        // True if ALL the specified tags are present in the expense
        return expense -> tagNames.stream()
                .allMatch(tagName -> expense.getTags().stream()
                        .anyMatch(tag -> tag.getTagName().equals(tagName)));
    }
}

