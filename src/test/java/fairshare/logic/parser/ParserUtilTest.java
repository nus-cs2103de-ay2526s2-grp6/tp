package fairshare.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fairshare.logic.parser.exceptions.ParseException;
import fairshare.model.expense.Participant;
import fairshare.model.tag.Tag;

public class ParserUtilTest {

    // Test tokenize returns a map.
    @Test
    public void tokenize_validArgs_returnsMap() throws ParseException {
        String args = "n/lunch a/15 p/john";
        Map<String, List<String>> map = ParserUtil.tokenize(args);

        assertTrue(map.containsKey("n"));
        assertEquals("lunch", map.get("n").getFirst());

        assertTrue(map.containsKey("a"));
        assertEquals("15", map.get("a").getFirst());

        assertTrue(map.containsKey("p"));
        assertEquals("john", map.get("p").getFirst());
    }

    // Test tokenize collates duplicate prefixes (flags) into a single list.
    @Test
    public void tokenize_duplicatePrefixes_collatesData() throws ParseException {
        String args = "s/john s/mary s/rowan";
        Map<String, List<String>> map = ParserUtil.tokenize(args);

        assertTrue(map.containsKey("s"));
        assertEquals(3, map.get("s").size());
        assertTrue(map.get("s").contains("john"));
        assertTrue(map.get("s").contains("mary"));
        assertTrue(map.get("s").contains("rowan"));
    }

    // Test singular prefix with invalid format.
    @Test
    public void tokenize_singlePrefixInvalidFormat_throwsParseException() {
        String args = " n:lunch";
        assertThrows(ParseException.class, () -> ParserUtil.tokenize(args));
    }

    // Test singular invalid prefix.
    @Test
    public void tokenize_singleInvalidPrefix_throwsParseException() {
        String args = " k/lunch";
        assertThrows(ParseException.class, () -> ParserUtil.tokenize(args));
    }

    // Test getSingleFieldData returns string when key is present.
    @Test
    public void getSingleFieldData_keyPresent_returnsString() throws ParseException {
        Map<String, List<String>> map = new HashMap<>();
        map.put("n", List.of("lunch"));

        String result = ParserUtil.getSingleFieldData(map, "n");
        assertEquals("lunch", result);
    }

    // Test getSingleFieldData key not present in map.
    @Test
    public void getSingleFieldData_keyNotPresent_throwsParseException() {
        Map<String, List<String>> map = new HashMap<>();

        assertThrows(ParseException.class, () -> ParserUtil.getSingleFieldData(map, "n"));
    }

    // Test getMultiFieldData returns a list if key is present.
    @Test
    public void getMultiFieldData_keyPresent_returnsList() throws ParseException {
        Map<String, List<String>> map = new HashMap<>();
        map.put("s", List.of("john", "mary", "luke"));

        List<String> result = ParserUtil.getMultiFieldData(map, "s");
        assertEquals(3, result.size());
        assertEquals("john", result.get(0));
        assertEquals("mary", result.get(1));
        assertEquals("luke", result.get(2));
    }

    // Test getMultiFieldData key not present in map.
    @Test
    public void getMultiFieldData_keyNotPresent_throwsParseException() {
        Map<String, List<String>> map = new HashMap<>();

        assertThrows(ParseException.class, () -> ParserUtil.getMultiFieldData(map, "s"));
    }

    // Test getOptionalSingleFieldData returns optional string when key is present.
    @Test
    public void getOptionalSingleFieldData_keyPresent_returnsOptional() throws ParseException {
        Map<String, List<String>> map = new HashMap<>();
        map.put("s", List.of("john"));
        Optional<String> result = ParserUtil.getOptionalSingleFieldData(map, "s");

        assertTrue(result.isPresent());
        assertEquals("john", result.get());
    }

    // Test getOptionalSingleFieldData returns an empty optional if key not present.
    @Test
    public void getOptionalSingleFieldData_keyNotPresent_returnsEmptyOptional() throws ParseException {
        Map<String, List<String>> map = new HashMap<>();
        Optional<String> result = ParserUtil.getOptionalSingleFieldData(map, "s");

        assertTrue(result.isEmpty());
    }

    // Test getOptionalSingleFieldData if key is present but value is empty e.g., "n/ ".
    @Test
    public void getOptionalSingleFieldData_keyPresentEmptyValue_throwsParseException() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("n", List.of(""));
        assertThrows(ParseException.class, () -> ParserUtil.getOptionalSingleFieldData(map, "n"));
    }

    // Test getOptionalMultiFieldData returns optional List<String> when key is present.
    @Test
    public void getOptionalMultiFieldData_keyPresent_returnsOptional() throws ParseException {
        Map<String, List<String>> map = new HashMap<>();
        map.put("s", List.of("john", "mary"));
        Optional<List<String>> result = ParserUtil.getOptionalMultiFieldData(map, "s");

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals("john", result.get().get(0));
        assertEquals("mary", result.get().get(1));
    }

    // Test getOptionalMultiFieldData returns an empty optional if key not present.
    @Test
    public void getOptionalMultiFieldData_keyNotPresent_returnsEmptyOptional() throws ParseException {
        Map<String, List<String>> map = new HashMap<>();
        Optional<List<String>> result = ParserUtil.getOptionalMultiFieldData(map, "s");

        assertTrue(result.isEmpty());
    }

    // Test getOptionalMultiFieldData if args contain empty string.
    @Test
    public void getOptionalMultiFieldData_containsEmptyString_throwsParseException() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("s", List.of("alice", ""));
        assertThrows(ParseException.class, () -> ParserUtil.getOptionalMultiFieldData(map, "s"));
    }

    // Test parse valid expense id.
    @Test
    public void parseExpenseId_validId_returnsInt() throws ParseException {
        assertEquals(2, ParserUtil.parseExpenseId("2"));
    }

    // Test parse invalid expense id.
    @Test
    public void parseExpenseId_invalidId_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseExpenseId("abc"));
        assertThrows(ParseException.class, () -> ParserUtil.parseExpenseId("1.5"));
    }

    // Test parse valid expense amount (cost).
    @Test
    public void parseAmount_validAmount_returnsDouble() throws ParseException {
        assertEquals(10.5, ParserUtil.parseAmount("10.5"));
        assertEquals(5.0, ParserUtil.parseAmount("5"));
    }

    // Test parse invalid expense amount.
    @Test
    public void parseAmount_invalidAmount_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAmount("ten"));
    }

    // Test parse an amount that is less than zero.
    @Test
    public void parseAmount_lessThanZero_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAmount("0"));
        assertThrows(ParseException.class, () -> ParserUtil.parseAmount("-5.00"));
    }

    // Test parse participants with no specified proportion (equal split).
    @Test
    public void parseParticipants_validNoProportion_returnsDefaultShares() throws ParseException {
        List<String> rawParticipants = List.of("alice", "john");
        Set<Participant> result = ParserUtil.parseParticipants(rawParticipants);

        assertEquals(2, result.size());

        Participant alice = result.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("alice"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("alice should be in the set"));
        assertEquals(1, alice.getShares());

        Participant john = result.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("john"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("john should be in the set"));
        assertEquals(1, john.getShares());
    }

    // Test parse participants with specified proportions (uneven split).
    @Test
    public void parseParticipants_validProportion_returnsSpecifiedShares() throws ParseException {
        List<String> rawParticipants = List.of("john:2", "alice:3");
        Set<Participant> result = ParserUtil.parseParticipants(rawParticipants);

        assertEquals(2, result.size());

        Participant john = result.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("john"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("john should be in the set"));
        assertEquals(2, john.getShares());

        Participant alice = result.stream()
                .filter(p -> p.getPerson().getName().equalsIgnoreCase("alice"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("alice should be in the set"));
        assertEquals(3, alice.getShares());
    }

    // Test parse participants with invalid specified proportions.
    @Test
    public void parseParticipants_invalidProportion_throwsParseException() {
        List<String> rawParticipants = List.of("mary:abc");
        assertThrows(ParseException.class, () -> ParserUtil.parseParticipants(rawParticipants));
    }

    // Test parse participants with specified proportions less than zero.
    @Test
    public void parseParticipants_lessThanZeroProportion_throwsParseException() {
        List<String> zeroShares = List.of("john:0");
        assertThrows(ParseException.class, () -> ParserUtil.parseParticipants(zeroShares));

        List<String> negativeShares = List.of("mary:-2");
        assertThrows(ParseException.class, () -> ParserUtil.parseParticipants(negativeShares));
    }

    // Test parse tags with specified tag names.
    @Test
    public void parseTags_validTags_returnsTagList() {
        List<String> rawTags = List.of("food", "transport");
        Set<Tag> result = ParserUtil.parseTags(rawTags);

        assertEquals(2, result.size());
        assertTrue(result.contains(new Tag("food")));
        assertTrue(result.contains(new Tag("transport")));
    }
}
