package fairshare.model.person;

/**
 * Represents a person participating in an expense.
 */
public class Person {
    private final String name;

    /**
     * Constructs an instance of {@code Person} with the specified name.
     *
     * @param name The name of the person.
     */
    public Person(String name) {
        this.name = name.trim();
    }

    /**
     * Returns the name of the person.
     *
     * @return The name string.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return this.name.equalsIgnoreCase(otherPerson.name); // case-insensitive
    }

    @Override
    public int hashCode() {
        // Any two person objects with the same name (case-insensitive) is assumed to be the same person (same hashcode)
        return this.getName().toLowerCase().hashCode();
    }
}
