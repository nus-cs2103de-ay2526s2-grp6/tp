package fairshare.model.group;

/**
 * Represents an expense group. Every expense belongs to exactly one group.
 */
public class Group {
    private final String groupName;

    /**
     * Creates an instance of {@code Group} with the specified name.
     *
     * @param groupName The group name.
     */
    public Group(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Returns the name of the group.
     *
     * @return The group name string.
     */
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return this.groupName.equals(otherGroup.getGroupName()); // Same group if they share the same name
    }

    @Override
    public int hashCode() {
        return this.groupName.hashCode();
    }
}
