package fairshare.model.tag;

/**
 * Represents a categorical tag for an expense.
 */
public class Tag {
    private final String tagName;

    /**
     * Creates an instance of {@code Tag} with the specified name.
     *
     * @param tagName The name of the tag.
     */
    public Tag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Returns the name of this tag.
     *
     * @return The tag name as a string.
     */
    public String getTagName() {
        return this.tagName;
    }
}
