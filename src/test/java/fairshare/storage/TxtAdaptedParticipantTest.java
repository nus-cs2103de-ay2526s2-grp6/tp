package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fairshare.model.expense.Participant;
import fairshare.model.person.Person;

public class TxtAdaptedParticipantTest {

    @Test
    public void serialise_validParticipant_correctFormat() {
        TxtAdaptedParticipant participant =
                new TxtAdaptedParticipant(
                        new TxtAdaptedPerson("bob"), 2);
        assertEquals("bob:2", participant.serialize());
    }

    @Test
    public void deserialise_validString_correctParticipant() {
        TxtAdaptedParticipant result =
                TxtAdaptedParticipant.deserialize("bob:2");
        assertEquals("bob", result.getName());
        assertEquals(2, result.getShares());
    }

    @Test
    public void deserialise_stringWithSpaces_returnsTrimmed() {
        TxtAdaptedParticipant result =
                TxtAdaptedParticipant.deserialize(" bob : 2 ");
        assertEquals("bob", result.getName());
        assertEquals(2, result.getShares());
    }

    @Test
    public void deserialise_missingShares_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                TxtAdaptedParticipant.deserialize("bob"));
    }

    @Test
    public void deserialise_invalidSharesFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                TxtAdaptedParticipant.deserialize("bob:abc"));
    }

    @Test
    public void toModelType_correctParticipant() {
        TxtAdaptedParticipant adapted =
                new TxtAdaptedParticipant(
                        new TxtAdaptedPerson("bob"), 2);
        Participant result = adapted.toModelType();
        assertEquals("bob", result.getPerson().getName());
        assertEquals(2, result.getShares());
    }

    @Test
    public void constructor_fromParticipant_correctFields() {
        Participant participant = new Participant(
                new Person("bob"), 2);
        TxtAdaptedParticipant adapted =
                new TxtAdaptedParticipant(participant);
        assertEquals("bob:2", adapted.serialize());
    }

    @Test
    public void serialiseAndDeserialize_roundTrip_sameData() {
        TxtAdaptedParticipant original =
                new TxtAdaptedParticipant(
                        new TxtAdaptedPerson("alice"), 3);
        String serialised = original.serialize();
        TxtAdaptedParticipant result =
                TxtAdaptedParticipant.deserialize(serialised);
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getShares(), result.getShares());
    }
}
