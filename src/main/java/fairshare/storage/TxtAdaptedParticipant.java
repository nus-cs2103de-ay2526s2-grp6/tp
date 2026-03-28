package fairshare.storage;

import fairshare.model.expense.Participant;

/**
 * A plain text-friendly version of {@code Participant}.
 * Used for serializing and deserializing participant data to and from the local storage file.
 */
public class TxtAdaptedParticipant {

    private final TxtAdaptedPerson person;
    private final int shares;

    /**
     * Constructs a {@code TxtAdaptedParticipant} from a {@code Participant} object.
     *
     * @param source The {@code Participant} to adapt.
     */
    public TxtAdaptedParticipant(Participant source) {
        this.person = new TxtAdaptedPerson(source.getPerson());
        this.shares = source.getShares();
    }

    /**
     * Constructs a {@code TxtAdaptedParticipant} with the specified adapted person and share weight.
     *
     * @param person The {@code TxtAdaptedPerson} representing the participant's identity.
     * @param shares The integer share weight of the participant.
     */
    public TxtAdaptedParticipant(TxtAdaptedPerson person, int shares) {
        this.person = person;
        this.shares = shares;
    }

    /**
     * Converts this adapted participant back into a {@code Participant} object.
     *
     * @return the corresponding {@code Participant}.
     */
    public Participant toModelType() {
        return new Participant(person.toModelType(), shares);
    }

    /**
     * Serializes this participant into a plain-text string.
     *
     * @return a string representation of this participant.
     */
    public String serialize() {
        return person.serialize() + ":" + shares;
    }

    /**
     * Deserializes a plain-text string into a {@code TxtAdaptedParticipant}.
     *
     * @param data the string to parse.
     * @return the corresponding {@code TxtAdaptedParticipant}.
     */
    public static TxtAdaptedParticipant deserialize(String data) {
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
