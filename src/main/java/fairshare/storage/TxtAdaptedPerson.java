package fairshare.storage;

/**
 * A plain text-friendly representation of a person.
 * Used for serializing and deserializaing person data to and from the local storage file.
 */

public class TxtAdaptedPerson {
    private final String name;

    /**
     * Constructs a {@code TxtAdaotedPerson} with the given name.
     *
     * @param name of the person; cannot be null or empty.
     */

    public TxtAdaptedPerson(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String serialise() {
        return name;
    }

    /**
     * Deserialises a plain-text string into a {@code TxtAdaptedPerson}.
     *
     * @param data the string to parse; cannot be null.
     * @return the corresponding {@code TxtAdaptedPerson}.
     */
    public static TxtAdaptedPerson deserialise(String data) {
        return new TxtAdaptedPerson(data.trim());
    }
}
