package fairshare.storage;

import fairshare.model.expense.Participant;

/**
 * A plain-text-friendly representation of a {@code Participant}.
 * Serialised as {@code name:shares} e.g. {@code bob:2}.
 */
public class TxtAdaptedParticipant {

    private final TxtAdaptedPerson person;
    private final int shares;

    /**
     * Constructs a {@code TxtAdaptedParticipant} from a
     * {@code Participant} model object.
     *
     * @param source the {@code Participant} to adapt; cannot be null.
     */
    public TxtAdaptedParticipant(Participant source) {
        this.person = new TxtAdaptedPerson(source.getPerson());
        this.shares = source.getShares();
    }

    /**
     * Constructs a {@code TxtAdaptedParticipant} with the given person
     * and shares.
     *
     * @param person the adapted person; cannot be null.
     * @param shares the number of shares; must be greater than 0.
     */
    public TxtAdaptedParticipant(TxtAdaptedPerson person, int shares) {
        this.person = person;
        this.shares = shares;
    }

    /**
     * Converts this adapted participant back into a {@code Participant}
     * model object.
     *
     * @return the corresponding {@code Participant}.
     */
    public Participant toModelType() {
        return new Participant(person.toModelType(), shares);
    }

    /**
     * Serialises this participant into a plain-text string
     * in the format {@code name:shares}.
     *
     * @return a string representation of this participant.
     */
    public String serialize() {
        return person.serialize() + ":" + shares;
    }

    /**
     * Deserialises a plain-text string into a
     * {@code TxtAdaptedParticipant}.
     * Expected format: {@code name:shares} e.g. {@code bob:2}.
     *
     * @param data the string to parse; cannot be null.
     * @return the corresponding {@code TxtAdaptedParticipant}.
     * @throws IllegalArgumentException if the format is invalid.
     */
    public static TxtAdaptedParticipant deserialise(String data) {
        String[] parts = data.split(":", 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException(
                    "Invalid participant format: " + data);
        }

        TxtAdaptedPerson person =
                TxtAdaptedPerson.deserialize(parts[0]);
        try {
            int shares = Integer.parseInt(parts[1].trim());
            return new TxtAdaptedParticipant(person, shares);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid shares format: " + parts[1]);
        }
    }
}
