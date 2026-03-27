package fairshare.storage;

import fairshare.model.expense.Participant;

public class TxtAdaptedParticipant {
    private final TxtAdaptedPerson person;
    private final int shares;

    public TxtAdaptedParticipant(Participant source) {
        this.person = new TxtAdaptedPerson(source.getPerson());
        this.shares = source.getShares();
    }

    public TxtAdaptedParticipant(TxtAdaptedPerson person, int shares) {
        this.person = person;
        this.shares = shares;
    }

    public Participant toModelType() {
        return new Participant(person.toModelType(), shares);
    }

    public String serialize() {
        return person.serialize() + ":" + shares;
    }

    /**
     * Returns the name of this participant.
     *
     * @return the name string.
     */
    public String getName() {
        return person.getName();
    }

    /**
     * Returns the number of shares of this participant.
     *
     * @return the shares as an int.
     */
    public int getShares() {
        return shares;
    }

    public static TxtAdaptedParticipant deserialize(String data) {
        String[] parts = data.trim().split(":", 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException(
                    "Invalid participant format: " + data);
        }

        TxtAdaptedPerson person =
                TxtAdaptedPerson.deserialize(parts[0].trim());
        try {
            int shares = Integer.parseInt(parts[1].trim());
            return new TxtAdaptedParticipant(person, shares);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid shares format: " + parts[1]);
        }
    }
}
