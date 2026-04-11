package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fairshare.model.group.Group;

public class TxtAdaptedGroupTest {

    @Test
    public void serialize_validGroup_returnsGroupName() {
        TxtAdaptedGroup group = new TxtAdaptedGroup("malaysia");
        assertEquals("malaysia", group.serialize());
    }

    @Test
    public void deserialize_validString_returnsCorrectGroup() {
        TxtAdaptedGroup group = TxtAdaptedGroup.deserialize("malaysia");
        assertEquals("malaysia", group.getGroupName());
    }

    @Test
    public void deserialize_stringWithSpaces_returnsTrimmedGroup() {
        TxtAdaptedGroup group =
                TxtAdaptedGroup.deserialize("  malaysia  ");
        assertEquals("malaysia", group.getGroupName());
    }

    @Test
    public void toModelType_validAdaptedGroup_returnsCorrectGroup() {
        TxtAdaptedGroup adapted = new TxtAdaptedGroup("malaysia");
        Group group = adapted.toModelType();
        assertEquals("MALAYSIA", group.getGroupName());
    }

    @Test
    public void constructor_fromGroup_correctGroupName() {
        Group group = new Group("malaysia");
        TxtAdaptedGroup adapted = new TxtAdaptedGroup(group);
        assertEquals("MALAYSIA", adapted.getGroupName());
    }

    @Test
    public void serialiseAndDeserialize_roundTrip_sameData() {
        TxtAdaptedGroup original = new TxtAdaptedGroup("japan");
        String serialised = original.serialize();
        TxtAdaptedGroup result = TxtAdaptedGroup.deserialize(serialised);
        assertEquals(original.getGroupName(), result.getGroupName());
    }
}
