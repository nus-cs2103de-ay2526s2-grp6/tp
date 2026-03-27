package fairshare.model.expense;

import fairshare.model.person.Person;

/**
 * Represents a participant in an expense, bundling a {@code Person} with their share weight for debt splitting.
 */
public class Participant {
    private Person person;
    private int shares;

    /**
     * Creates an instance of {@code Participant} with the specified person and share weight.
     *
     * @param person The person participating in the expense.
     * @param shares The number of shares (weight) this person holds in the expense split.
     */
    public Participant(Person person, int shares) {
        this.person = person;
        this.shares = shares;
    }

    /**
     * Returns the person participating in the expense.
     *
     * @return The {@code Person} object.
     */
    public Person getPerson() {
        return this.person;
    }

    /**
     * Returns the proportional weight this participant holds in the expense.
     *
     * @return The integer number of shares.
     */
    public int getShares() {
        return this.shares;
    }
}
