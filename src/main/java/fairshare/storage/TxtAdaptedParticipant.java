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

    public static TxtAdaptedParticipant deserialize(String data) {
        String[] parts = data.split(":", 2);

        TxtAdaptedPerson person = TxtAdaptedPerson.deserialize(parts[0]);
        try {
            int shares = Integer.parseInt(parts[1]);
            return new TxtAdaptedParticipant(person, shares);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid shares format");
        }
    }
}
