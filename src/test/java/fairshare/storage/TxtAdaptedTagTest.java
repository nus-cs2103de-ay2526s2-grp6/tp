package fairshare.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fairshare.model.tag.Tag;

public class TxtAdaptedTagTest {

    @Test
    public void serialize_validTag_returnsTagName() {
        TxtAdaptedTag tag = new TxtAdaptedTag("food");
        assertEquals("food", tag.serialize());
    }

    @Test
    public void deserialize_validString_returnsCorrectTag() {
        TxtAdaptedTag tag = TxtAdaptedTag.deserialize("food");
        assertEquals("food", tag.getTagName());
    }

    @Test
    public void deserialize_stringWithSpaces_returnsTrimmedTag() {
        TxtAdaptedTag tag = TxtAdaptedTag.deserialize("  food  ");
        assertEquals("food", tag.getTagName());
    }

    @Test
    public void toModelType_validAdaptedTag_returnsCorrectTag() {
        TxtAdaptedTag adapted = new TxtAdaptedTag("food");
        Tag tag = adapted.toModelType();
        assertEquals("food", tag.getTagName());
    }

    @Test
    public void constructor_fromTag_correctTagName() {
        Tag tag = new Tag("food");
        TxtAdaptedTag adapted = new TxtAdaptedTag(tag);
        assertEquals("food", adapted.getTagName());
    }
}
