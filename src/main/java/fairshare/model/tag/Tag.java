package fairshare.model.tag;

import fairshare.model.person.Person;

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
        this.tagName = tagName.toLowerCase();
    }

    /**
     * Returns the name of this tag.
     *
     * @return The tag name as a string.
     */
    public String getTagName() {
        return this.tagName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        String myTagName = this.getTagName().trim();
        String theirTagName = otherTag.getTagName().trim();

        return myTagName.equals(theirTagName);
    }

    @Override
    public int hashCode() {
        // Any two tag objects with the same name is assumed to be the same tag (same hashcode)
        return this.getTagName().trim().hashCode();
    }
}
