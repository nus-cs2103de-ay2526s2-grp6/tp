package fairshare.storage;

import fairshare.model.person.Person;

/**
 * A plain text-friendly representation of a {@code Person}.
 * Used for serializing and deserializing person data to and from the local storage file.
 */
public class TxtAdaptedPerson {
    private final String name;

    /**
     * Constructs a {@code TxtAdaptedPerson} with the given name.
     *
     * @param name the name of the person; cannot be null or empty.
     */
    public TxtAdaptedPerson(String name) {
        this.name = name;
    }

    /**
     * Constructs a {@code TxtAdaptedPerson} from a {@code Person} model object.
     * @param source the {@code Person} to adapt; cannot be null.
     */
    public TxtAdaptedPerson(Person source) {
        this.name = source.getName();
    }

    /**
     * Returns the name of this person.
     *
     * @return the name string.
     */
    public String getName() {
        return name;
    }

    /**
     * Converts this adapted person back into a {@code Person} model object.
     *
     * @return the corresponding {@code Person}.
     */
    public Person toModelType() {
        return new Person(name);
    }

    /**
     * Serializes this person into a plain-text string.
     *
     * @return a string representation of this person.
     */
    public String serialize() {
        return name;
    }

    /**
     * Deserializes a plain-text string into a {@code TxtAdaptedPerson}.
     *
     * @param data the string to parse; cannot be null.
     * @return the corresponding {@code TxtAdaptedPerson}.
     */
    public static TxtAdaptedPerson deserialize(String data) {
        return new TxtAdaptedPerson(data.trim());
    }
}
