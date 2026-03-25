package fairshare.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fairshare.logic.parser.exceptions.ParseException;

public class ParserUtil {

    public static Map<String, List<String>> tokenize(String args) throws ParseException {
        Map<String, List<String>> map = new HashMap<>();

        String[] tokens = args.split(" (?=[a-z]/)");

        for (String token : tokens) {
            String[] parts = token.split("/", 2);
            if (parts.length < 2) {
                throw new ParseException("Invalid filter: " + args + ", Expected: prefix/data");
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

    public static String getSingleFieldData(Map<String, List<String>> map, String key) throws ParseException {
        List<String> data = map.get(key);
        if (data == null || data.isEmpty()) {
            throw new ParseException("Missing mandatory field: " + key + "/");
        }
        return data.getFirst();
    }

    public static List<String> getMultiFieldData(Map<String, List<String>> map, String key) throws ParseException {
        List<String> data = map.get(key);
        if (data == null || data.isEmpty()) {
            throw new ParseException("Missing mandatory field: " + key + "/");
        }
        return data;
    }

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
}
