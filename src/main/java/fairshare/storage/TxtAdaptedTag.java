package fairshare.storage;

import fairshare.model.tag.Tag;

/**
 * A plain text-friendly representation of a {@code Tag}.
 * Used for serializing and deserializing tag data to and from the local storage file.
 */

public class TxtAdaptedTag {
    private final String tagName;

    /**
     * Constructs a {@code TxtAdaptedTag} with the given name.
     *
     * @param tagName the name of the tag; cannot be null or empty.
     */
    public TxtAdaptedTag(String tagName) {
        assert tagName != null && !tagName.isBlank() : "tagName should not be null or blank";

        this.tagName = tagName;
    }

    /**
     * Constrcuts a {@code TxtAdaptedTag} from a {@code Tag} model object.
     *
     * @param source the {@code Tag} to adapt; cannot be null.
     */
    //AI declaration --> this idea was adapted from claude ai
    public TxtAdaptedTag(Tag source) {
        this.tagName = source.getTagName();
    }

    /**
     * Returns the tag name.
     *
     * @return the tag name string.
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Converts this adapted tag back into a {@code Tag} model object.
     *
     * @return the corresponding {@code Tag}.
     */
    public Tag toModelType() {
        return new Tag(tagName);
    }

    /**
     * Serializes this tag into a plain-text string.
     *
     * @return a string representation of this tag.
     */
    public String serialize() {
        return tagName;
    }

    /**
     * Deserializes a plain-text string into a {@code TxtAdaptedTag}.
     *
     * @param data the string to parse; cannot be null.
     * @return the corresponding {@code TxtAdaptedTag}.
     */
    public static TxtAdaptedTag deserialize(String data) {
        return new TxtAdaptedTag(data.trim());
    }
}
