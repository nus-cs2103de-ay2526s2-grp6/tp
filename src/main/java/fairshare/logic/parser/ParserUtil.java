package fairshare.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Participant;
import fairshare.model.person.Person;
import fairshare.model.tag.Tag;

/**
 * Represents a class containing utility methods for parsing user input in the various Parser classes.
 */
public class ParserUtil {

    /**
     * Tokenizes a raw string of arguments into a map of prefixes to their respective data values.
     *
     * @param args The raw argument string from the user input.
     * @return A map where each key is a prefix (e.g., "n", "p") and the value is a list of data strings.
     * @throws ParseException If the argument formatting is invalid.
     */
    public static Map<String, List<String>> tokenize(String args) throws ParseException {
        Map<String, List<String>> map = new HashMap<>();

        String[] tokens = args.split(" (?=[a-z]/)");

        for (String token : tokens) {
            String[] parts = token.split("/", 2);
            if (parts.length < 2) {
                throw new ParseException("Invalid flag: " + args + ", Expected: prefix/data");
            }

            String argType = parts[0];
            String argData = parts[1];
            List<String> data;
            if (!map.containsKey(argType)) {
                data = new ArrayList<>(List.of(argData));
            } else {
                data = map.get(argType);
                data.add(argData);
            }
            map.put(argType, data);
        }
        return map;
    }

    /**
     * Retrieves the mandatory data mapped to the specified prefix key (flag).
     * This method is intended for "single-field" arguments where the prefix should only correspond
     * to one distinct value (e.g., expense name, payer).
     *
     * @param map The tokenized argument map.
     * @param key The prefix key to look up (e.g., "n" for name).
     * @return The string data associated with the given key.
     * @throws ParseException If multiple values are detected, the key is missing, or the data is empty.
     */
    public static String getSingleFieldData(Map<String, List<String>> map, String key) throws ParseException {
        List<String> data = map.get(key);
        if (data == null || data.isEmpty()) {
            throw new ParseException("Missing mandatory field: " + key + "/");
        }

        if (data.size() > 1) {
            throw new ParseException("Multiple values detected for " + key + "/. Only one is allowed.");
        }

        return data.getFirst();
    }

    /**
     * Retrieves the mandatory data mapped to the specified prefix key (flag) as a list.
     * This method is intended for "multi-field" arguments where the user is allowed to type
     * the same prefix multiple times in a single command to build a list (e.g., participants, tags).
     *
     * @param map The tokenized argument map.
     * @param key The prefix key to look up (e.g., "t" for tags).
     * @return A list of all string values associated with the key.
     * @throws ParseException If the key is missing or the data is empty.
     */
    public static List<String> getMultiFieldData(Map<String, List<String>> map, String key) throws ParseException {
        List<String> data = map.get(key);
        if (data == null || data.isEmpty()) {
            throw new ParseException("Missing mandatory field: " + key + "/");
        }
        return data;
    }

    /**
     * Retrieves the optional data mapped to the specified prefix key.
     *
     * @param map The tokenized argument map.
     * @param key The prefix key to look up.
     * @return An {@code Optional} containing the string data if present, otherwise an empty {@code Optional}.
     * @throws ParseException If multiple values are detected or if the flag is present but no value is typed after it.
     */
    public static Optional<String> getOptionalSingleFieldData(
            Map<String, List<String>> map, String key) throws ParseException {
        List<String> data = map.get(key);
        if (data == null) {
            return Optional.empty();
        }

        if (data.size() > 1) {
            throw new ParseException("Multiple values detected for " + key + "/. Only one is allowed.");
        }

        String output = data.getFirst();
        if (output.isEmpty()) {
            throw new ParseException("Missing value for flag: " + key + "/");
        }

        return Optional.of(data.getFirst());
    }

    /**
     * Retrieves the optional data mapped to the specified prefix key as a list.
     *
     * @param map The tokenized argument map.
     * @param key The prefix key to look up.
     * @return An {@code Optional} containing a list of strings if present, otherwise an empty {@code Optional}.
     * @throws ParseException If a flag is present but contains an empty string.
     */
    public static Optional<List<String>> getOptionalMultiFieldData(
            Map<String, List<String>> map, String key) throws ParseException {
        List<String> data = map.get(key);
        if (data == null) {
            return Optional.empty();
        }
        if (data.contains("")) {
            throw new ParseException("Missing value for flag: " + key + "/");
        }
        return Optional.of(data);
    }

    /**
     * Parses a string representation of an expense id into an integer.
     *
     * @param expenseId The string representation of the index to be parsed.
     * @return The parsed integer index.
     * @throws ParseException If the string cannot be parsed into a valid integer.
     */
    public static int parseExpenseId(String expenseId) throws ParseException {
        try {
            return Integer.parseInt(expenseId);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid expense id: " + expenseId);
        }
    }

    /**
     * Parses a string representation of an amount into a double.
     *
     * @param amount The string representation of the amount.
     * @return The parsed double amount.
     * @throws ParseException If the string cannot be parsed into a double or is less than or equal to zero.
     */
    public static double parseAmount(String amount) throws ParseException {
        try {
            double parsedAmt = Double.parseDouble(amount);
            if (parsedAmt <= 0) {
                throw new ParseException("Amount must be greater than zero.");
            }
            return parsedAmt;
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid amount: " + amount);
        }
    }

    /**
     * Parses a list of raw participant strings into a list of {@code Participant} objects.
     *
     * @param strParticipants A list of unparsed participant strings
     * @return A set of {@code Participant} objets.
     * @throws ParseException If any of the participant strings are formatted incorrectly.
     */
    public static Set<Participant> parseParticipants(List<String> strParticipants) throws ParseException {
        boolean isEqualSplit = strParticipants.stream()
                .anyMatch(str -> !str.contains(":"));
        boolean isUnequalSplit = strParticipants.stream()
                .anyMatch(str -> str.contains(":"));

        if (isEqualSplit && isUnequalSplit) {
            throw new ParseException(
                    "Invalid format: If splitting by proportion, every participant must have a specified share.");
        }

        Set<Participant> participants = new HashSet<>();
        for (String p : strParticipants) {
            Participant newParticipant = parseParticipant(p);

            if (!participants.add(newParticipant)) {
                throw new ParseException("An expense cannot have duplicate participants.");
            }
        }

        return participants;
    }

    /**
     * Parses a list of raw tags into a set of {@code Tag} objects.
     *
     * @param tags A list of unparsed tag strings.
     * @return A set of {@code Tag} objects.
     */
    public static Set<Tag> parseTags(List<String> tags) {
        return tags.stream()
                .map(tagName -> new Tag(tagName))
                .collect(Collectors.toSet());
    }

    private static Participant parseParticipant(String strParticipant) throws ParseException {
        if (strParticipant.trim().endsWith(":")) {
            throw new ParseException("Missing shares: Please specify shares for every participant.");
        }

        String[] parts = strParticipant.split(":");
        String participantName = parts[0];

        if (parts.length == 1) {
            return new Participant(new Person(participantName), 1);
        }

        String strShares = parts[1];
        try {
            int shares = Integer.parseInt(strShares);
            if (shares <= 0) {
                throw new ParseException("Expense participant's share must be greater than 0.");
            }
            return new Participant(new Person(participantName), shares);
        } catch (NumberFormatException e) {
            throw new ParseException("Expense participant's share must be an integer (e.g., s/john:3)");
        }
    }
}
