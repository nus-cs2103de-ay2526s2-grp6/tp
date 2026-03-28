package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fairshare.model.person.Person;

public class TxtAdaptedPersonTest {

    @Test
    public void serialise_validPerson_returnsName() {
        TxtAdaptedPerson person = new TxtAdaptedPerson("alice");
        assertEquals("alice", person.serialize());
    }

    @Test
    public void deserialise_validString_returnsCorrectPerson() {
        TxtAdaptedPerson person = TxtAdaptedPerson.deserialize("alice");
        assertEquals("alice", person.getName());
    }

    @Test
    public void deserialise_stringWithSpaces_returnsTrimmedPerson() {
        TxtAdaptedPerson person =
                TxtAdaptedPerson.deserialize("  alice  ");
        assertEquals("alice", person.getName());
    }

    @Test
    public void toModelType_validAdaptedPerson_returnsCorrectPerson() {
        TxtAdaptedPerson adapted = new TxtAdaptedPerson("alice");
        Person person = adapted.toModelType();
        assertEquals("alice", person.getName());
    }

    @Test
    public void constructor_fromPerson_correctName() {
        Person person = new Person("alice");
        TxtAdaptedPerson adapted = new TxtAdaptedPerson(person);
        assertEquals("alice", adapted.getName());
    }
}
