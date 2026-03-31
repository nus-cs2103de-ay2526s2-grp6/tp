package fairshare.storage;

import fairshare.model.group.Group;

/**
 * A plain text-friendly representation of a {@code Group}.
 * Used for serializing and deserializing person data to and from the local storage file.
 */
public class TxtAdaptedGroup {
    private final String groupName;

    /**
     * Creates an instance of {@code TxtAdaptedGroup} with the given group name.
     *
     * @param groupName the name of the group.
     */
    public TxtAdaptedGroup(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Creates an instance of {@code TxtAdaptedGroup} from a {@code Group} object.
     *
     * @param source the {@code Group} to adapt.
     */
    public TxtAdaptedGroup(Group source) {
        this.groupName = source.getGroupName();
    }

    /**
     * Returns the name of this group.
     *
     * @return the group name string.
     */
    public String getGroupName() {
        return this.groupName;
    }

    /**
     * Converts this adapted group back into a {@code Group} object.
     *
     * @return the corresponding {@code Group}.
     */
    public Group toModelType() {
        return new Group(this.groupName);
    }

    /**
     * Serializes this group into a plain-text string.
     *
     * @return a string presentation of this group.
     */
    public String serialize() {
        return this.groupName;
    }

    /**
     * Deserializes a plain-text string into a {@code TxtAdaptedGroup}.
     *
     * @param data the string to parse.
     * @return the corresponding {@code TxtAdaptedGroup}.
     */
    public static TxtAdaptedGroup deserialize(String data) {
        return new TxtAdaptedGroup(data.trim());
    }
}
